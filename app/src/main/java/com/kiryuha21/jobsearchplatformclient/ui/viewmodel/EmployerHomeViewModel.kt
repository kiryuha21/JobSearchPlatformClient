package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.DEFAULT_PAGE_SIZE
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.MoreItemsState
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.ResumeFilters
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.START_PAGE
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainResume
import com.kiryuha21.jobsearchplatformclient.data.mappers.toResumeFiltersDTO
import com.kiryuha21.jobsearchplatformclient.di.AuthToken
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.resumeRetrofit
import com.kiryuha21.jobsearchplatformclient.ui.contract.EmployerHomeContract
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmployerHomeViewModel(
    private val openResumeCallback: (String) -> Unit
) : BaseViewModel<EmployerHomeContract.Intent, EmployerHomeContract.State>() {
    override fun initialState(): EmployerHomeContract.State {
        return EmployerHomeContract.State(
            isLoading = true,
            moreItemsState = MoreItemsState.Undefined,
            areRecommendationsShown = true,
            pageNumber = 0,
            resumes = null,
            filters = ResumeFilters()
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is EmployerHomeContract.Intent.LoadResumes -> loadResumes(intent.filters)
            is EmployerHomeContract.Intent.OpenResumeDetails -> openResumeCallback(intent.resumeId)
            is EmployerHomeContract.Intent.LoadRecommendations -> loadRecommendations(intent.pageNumber)
        }
    }

    private fun resolvePostLoadState(isFirstLoad: Boolean, newResumes: List<Resume>?) {
        if (newResumes != null) {
            setState { copy(
                isLoading = false,
                resumes = if (isFirstLoad) {
                    newResumes
                } else {
                    (viewState.resumes ?: throw Exception("resumes can't be null here")) + newResumes
                },
                moreItemsState = if (newResumes.size < DEFAULT_PAGE_SIZE) MoreItemsState.Unavailable else MoreItemsState.Available
            ) }
        } else {
            setState { copy(isLoading = false) }
        }
    }

    private fun loadResumes(filters: ResumeFilters) {
        viewModelScope.launch {
            val isFirstLoad = filters.pageRequestFilter.pageNumber == START_PAGE

            if (isFirstLoad) {
                setState { copy(isLoading = true, filters = filters, pageNumber = START_PAGE, areRecommendationsShown = false) }
            } else {
                setState { copy(filters = filters, pageNumber = filters.pageRequestFilter.pageNumber, areRecommendationsShown = false) }
            }

            val resumesResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { resumeRetrofit.getFilteredResumes(filters.toResumeFiltersDTO()).map { it.toDomainResume() } }
                )
            }

            resolvePostLoadState(isFirstLoad, resumesResult)
        }
    }

    private fun loadRecommendations(pageNumber: Int) {
        val token = viewModelScope.async(Dispatchers.IO) {
            networkCallWithReturnWrapper(
                networkCall = { "Bearer ${AuthToken.getToken()}" }
            )
        }

        val isFirstLoad = pageNumber == START_PAGE
        viewModelScope.launch {
            if (isFirstLoad) {
                setState { copy(isLoading = true, pageNumber = pageNumber, areRecommendationsShown = true) }
            } else {
                setState { copy(pageNumber = pageNumber, areRecommendationsShown = true) }
            }

            val resumesResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { resumeRetrofit.getResumeRecommendations(
                        authToken = token.await() ?: throw Exception("token can't be null here"),
                        pageNumber = pageNumber
                    ) }
                )?.map { it.toDomainResume() }
            }

            resolvePostLoadState(isFirstLoad, resumesResult)
        }
    }

    companion object {
        fun provideFactory(openResumeCallback: (String) -> Unit) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return EmployerHomeViewModel(openResumeCallback) as T
                }
            }
    }
}