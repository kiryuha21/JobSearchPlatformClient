package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.DEFAULT_PAGE_SIZE
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.START_PAGE
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.VacancyFilters
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.MoreItemsState
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.resolveMoreItemsState
import com.kiryuha21.jobsearchplatformclient.data.local.dao.VacancyDAO
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainVacancy
import com.kiryuha21.jobsearchplatformclient.data.mappers.toVacancyEntity
import com.kiryuha21.jobsearchplatformclient.data.mappers.toVacancyFiltersDTO
import com.kiryuha21.jobsearchplatformclient.di.AuthToken
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.vacancyRetrofit
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerHomeContract
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkerHomeViewModel(
    private val openVacancyCallback: (String) -> Unit,
    private val vacancyDAO: VacancyDAO,
) : BaseViewModel<WorkerHomeContract.Intent, WorkerHomeContract.State>() {
    private var isCacheShown = true
    private var onlineSilentVacancies = emptyList<Vacancy>()
    private var fetchOnlineVacanciesJob = getSilentFetchJob()

    override fun initialState(): WorkerHomeContract.State {
        return WorkerHomeContract.State(
            isLoading = true,
            moreItemsState = MoreItemsState.Undefined,
            areRecommendationsShown = true,
            areOnlineRecommendationsReady = false,
            nextLoadPage = 0,
            vacancies = emptyList(),
            filters = VacancyFilters()
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is WorkerHomeContract.Intent.LoadVacancies -> loadVacancies(intent.filters)
            is WorkerHomeContract.Intent.LoadRecommendations -> loadRecommendations(intent.pageNumber)
            is WorkerHomeContract.Intent.OpenVacancyDetails -> openVacancyCallback(intent.vacancyId)
            is WorkerHomeContract.Intent.SwitchToOnlineRecommendations -> switchToOnlineRecommendations()
            is WorkerHomeContract.Intent.ResetPage -> resetPage()
        }
    }

    private fun getSilentFetchJob() =
        viewModelScope.launch(Dispatchers.IO) {
            onlineSilentVacancies = networkCallWithReturnWrapper(networkCall = {
                vacancyRetrofit.getVacancyRecommendations(
                    authToken = "Bearer ${AuthToken.getToken()}",
                    pageNumber = START_PAGE
                ).map { it.toDomainVacancy() }
            }) ?: emptyList()

            withContext(Dispatchers.Main) {
                setState { copy(areOnlineRecommendationsReady = true) }
            }
        }

    private fun resolvePostLoadState(pageNumber: Int, isOnlineLoad: Boolean, newVacancies: List<Vacancy>?) {
        val isFirstLoad = pageNumber == START_PAGE

        if (newVacancies != null) {
            setState { copy(
                isLoading = false,
                vacancies = if (isFirstLoad) {
                    newVacancies
                } else {
                    viewState.vacancies + newVacancies
                },
                nextLoadPage = pageNumber + 1,
                moreItemsState = resolveMoreItemsState(newVacancies.size, isOnlineLoad)
            ) }
        } else {
            setState { copy(isLoading = false) }
        }
    }

    private fun resetPage() {
        isCacheShown = true
        fetchOnlineVacanciesJob.cancel()
        fetchOnlineVacanciesJob = getSilentFetchJob()
        loadRecommendations(START_PAGE)
    }

    private fun switchToOnlineRecommendations() {
        isCacheShown = false
        setState { copy(
            vacancies = onlineSilentVacancies,
            areOnlineRecommendationsReady = false,
            moreItemsState = resolveMoreItemsState(onlineSilentVacancies.size, true),
            nextLoadPage = 1,
            isLoading = false
        ) }

        viewModelScope.launch(Dispatchers.IO) {
            vacancyDAO.upsertVacancies(onlineSilentVacancies.map { it.toVacancyEntity() })
        }
    }

    private fun loadVacancies(filters: VacancyFilters) {
        viewModelScope.launch {
            val isFirstLoad = filters.pageRequestFilter.pageNumber == START_PAGE

            if (isFirstLoad) {
                setState { copy(isLoading = true, filters = filters, areRecommendationsShown = false) }
            } else {
                setState { copy(filters = filters, areRecommendationsShown = false) }
            }

            val vacanciesResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { vacancyRetrofit.getFilteredVacancies(filters.toVacancyFiltersDTO()).map { it.toDomainVacancy() } }
                )
            }

            resolvePostLoadState(
                pageNumber = filters.pageRequestFilter.pageNumber,
                isOnlineLoad = true,
                newVacancies = vacanciesResult
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
                val cachedVacancies = withContext(Dispatchers.IO) {
                    vacancyDAO.getUserVacancies(CurrentUser.info.username, pageNumber * DEFAULT_PAGE_SIZE)
                }

                if (isFirstLoad && cachedVacancies.isEmpty()) {
                    fetchOnlineVacanciesJob.join()
                    switchToOnlineRecommendations()
                } else {
                    resolvePostLoadState(
                        pageNumber = pageNumber,
                        isOnlineLoad = false,
                        newVacancies = cachedVacancies.map { it.toDomainVacancy() }
                    )
                }
            } else {
                val loadedVacancies = withContext(Dispatchers.IO) {
                    networkCallWithReturnWrapper(networkCall = {
                        vacancyRetrofit.getVacancyRecommendations(
                            authToken = "Bearer ${AuthToken.getToken()}",
                            pageNumber = pageNumber
                        )
                    })
                }

                resolvePostLoadState(
                    pageNumber = pageNumber,
                    isOnlineLoad = true,
                    newVacancies = loadedVacancies?.map { it.toDomainVacancy() }
                )

                if (loadedVacancies != null) {
                    withContext(Dispatchers.IO) {
                        vacancyDAO.upsertVacancies(loadedVacancies.map { it.toVacancyEntity() })
                    }
                }
            }
        }
    }

    override fun onCleared() {
        fetchOnlineVacanciesJob.cancel()
        super.onCleared()
    }

    companion object {
        fun provideFactory(openVacancyCallback: (String) -> Unit, vacancyDAO: VacancyDAO) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return WorkerHomeViewModel(openVacancyCallback, vacancyDAO) as T
                }
            }
    }
}