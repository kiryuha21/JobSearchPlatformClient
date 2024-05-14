package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.DEFAULT_PAGE_SIZE
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.ResumeFilters
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.START_PAGE
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.MoreItemsState
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.resolveMoreItemsState
import com.kiryuha21.jobsearchplatformclient.data.local.dao.ResumeDAO
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainResume
import com.kiryuha21.jobsearchplatformclient.data.mappers.toResumeEntity
import com.kiryuha21.jobsearchplatformclient.data.mappers.toResumeFiltersDTO
import com.kiryuha21.jobsearchplatformclient.di.AuthToken
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.resumeRetrofit
import com.kiryuha21.jobsearchplatformclient.ui.contract.EmployerHomeContract
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmployerHomeViewModel(
    private val openResumeCallback: (String) -> Unit,
    private val resumeDAO: ResumeDAO,
) : BaseViewModel<EmployerHomeContract.Intent, EmployerHomeContract.State>() {
    private var isCacheShown = true
    private var onlineSilentResumes = emptyList<Resume>()
    private var fetchOnlineResumesJob = getSilentFetchJob()
    override fun initialState(): EmployerHomeContract.State {
        return EmployerHomeContract.State(
            isLoading = true,
            moreItemsState = MoreItemsState.Undefined,
            areRecommendationsShown = true,
            areOnlineRecommendationsReady = false,
            nextLoadPage = START_PAGE,
            resumes = emptyList(),
            filters = ResumeFilters()
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is EmployerHomeContract.Intent.LoadResumes -> loadResumes(intent.filters)
            is EmployerHomeContract.Intent.OpenResumeDetails -> openResumeCallback(intent.resumeId)
            is EmployerHomeContract.Intent.LoadRecommendations -> loadRecommendations(intent.pageNumber)
            is EmployerHomeContract.Intent.SwitchToOnlineRecommendations -> switchToOnlineRecommendations()
            is EmployerHomeContract.Intent.RefreshPage -> refreshPage()
        }
    }

    init {
        loadRecommendations(START_PAGE)
    }

    private fun getSilentFetchJob() =
        viewModelScope.launch(Dispatchers.IO) {
            onlineSilentResumes = networkCallWithReturnWrapper(networkCall = {
                resumeRetrofit.getResumeRecommendations(
                    authToken = "Bearer ${AuthToken.getToken()}",
                    pageNumber = START_PAGE
                ).map { it.toDomainResume() }
            }) ?: emptyList()

            withContext(Dispatchers.Main) {
                setState { copy(areOnlineRecommendationsReady = true) }
            }
        }

    private fun resolvePostLoadState(pageNumber: Int, isOnlineLoad: Boolean, newResumes: List<Resume>?) {
        val isFirstLoad = pageNumber == START_PAGE

        if (newResumes != null) {
            setState { copy(
                isLoading = false,
                resumes = if (isFirstLoad) {
                    newResumes
                } else {
                    viewState.resumes + newResumes
                },
                nextLoadPage = pageNumber + 1,
                moreItemsState = resolveMoreItemsState(newResumes.size, isOnlineLoad)
            ) }
        } else {
            setState { copy(isLoading = false) }
        }
    }

    private fun refreshPage() {
        isCacheShown = false
        setState { copy(areOnlineRecommendationsReady = false) }

        if (viewState.areRecommendationsShown) {
            loadRecommendations(START_PAGE)
        } else {
            loadResumes(filters = viewState.filters.copy(pageRequestFilter = viewState.filters.pageRequestFilter.copy(pageNumber = START_PAGE)))
        }
    }

    private fun switchToOnlineRecommendations() {
        isCacheShown = false
        setState { copy(
            resumes = onlineSilentResumes,
            areOnlineRecommendationsReady = false,
            moreItemsState = resolveMoreItemsState(onlineSilentResumes.size, true),
            nextLoadPage = 1,
            isLoading = false
        ) }

        viewModelScope.launch(Dispatchers.IO) {
            resumeDAO.upsertResumes(onlineSilentResumes.map { it.toResumeEntity() })
        }
    }

    private fun loadResumes(filters: ResumeFilters) {
        viewModelScope.launch {
            val isFirstLoad = filters.pageRequestFilter.pageNumber == START_PAGE

            if (isFirstLoad) {
                setState { copy(isLoading = true, filters = filters, areRecommendationsShown = false) }
            } else {
                setState { copy(filters = filters, areRecommendationsShown = false) }
            }

            val resumesResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { resumeRetrofit.getFilteredResumes(filters.toResumeFiltersDTO()).map { it.toDomainResume() } }
                )
            }

            resolvePostLoadState(
                pageNumber = filters.pageRequestFilter.pageNumber,
                isOnlineLoad = true,
                newResumes = resumesResult
            )
        }
    }

    private fun loadRecommendations(pageNumber: Int) {
        val isFirstLoad = pageNumber == START_PAGE

        viewModelScope.launch {
            if (isFirstLoad) {
                setState { copy(isLoading = true, areRecommendationsShown = true) }
            } else {
                setState { copy(areRecommendationsShown = true) }
            }

            if (isCacheShown) {
                val cachedResumes = withContext(Dispatchers.IO) {
                    resumeDAO.getUserResumes(CurrentUser.info.username, pageNumber * DEFAULT_PAGE_SIZE)
                }

                if (isFirstLoad && cachedResumes.isEmpty()) {
                    fetchOnlineResumesJob.join()
                    switchToOnlineRecommendations()
                } else {
                    resolvePostLoadState(
                        pageNumber = pageNumber,
                        isOnlineLoad = false,
                        newResumes = cachedResumes.map { it.toDomainResume() }
                    )
                }
            } else {
                val loadedResumes = withContext(Dispatchers.IO) {
                    networkCallWithReturnWrapper(networkCall = {
                        resumeRetrofit.getResumeRecommendations(
                            authToken = "Bearer ${AuthToken.getToken()}",
                            pageNumber = pageNumber
                        )
                    })
                }

                resolvePostLoadState(
                    pageNumber = pageNumber,
                    isOnlineLoad = true,
                    newResumes = loadedResumes?.map { it.toDomainResume() }
                )

                if (loadedResumes != null) {
                    withContext(Dispatchers.IO) {
                        resumeDAO.upsertResumes(loadedResumes.map { it.toResumeEntity() })
                    }
                }
            }
        }
    }

    override fun onCleared() {
        fetchOnlineResumesJob.cancel()
        super.onCleared()
    }

    companion object {
        fun provideFactory(openResumeCallback: (String) -> Unit, resumeDAO: ResumeDAO) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return EmployerHomeViewModel(openResumeCallback, resumeDAO) as T
                }
            }
    }
}