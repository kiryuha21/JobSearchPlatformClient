package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.DEFAULT_PAGE_SIZE
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.MoreItemsState
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.START_PAGE
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.VacancyFilters
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainVacancy
import com.kiryuha21.jobsearchplatformclient.data.mappers.toVacancyFiltersDTO
import com.kiryuha21.jobsearchplatformclient.di.AuthToken
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.vacancyRetrofit
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerHomeContract
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkerHomeViewModel(
    private val openVacancyCallback: (String) -> Unit
) : BaseViewModel<WorkerHomeContract.Intent, WorkerHomeContract.State>() {
    override fun initialState(): WorkerHomeContract.State {
        return WorkerHomeContract.State(
            isLoading = true,
            moreItemsState = MoreItemsState.Undefined,
            areRecommendationsShown = true,
            pageNumber = 0,
            vacancies = null,
            filters = VacancyFilters()
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is WorkerHomeContract.Intent.LoadVacancies -> loadVacancies(intent.filters)
            is WorkerHomeContract.Intent.LoadRecommendations -> loadRecommendations(intent.pageNumber)
            is WorkerHomeContract.Intent.OpenVacancyDetails -> openVacancyCallback(intent.vacancyId)
        }
    }

    private fun resolvePostLoadState(isFirstLoad: Boolean, newVacancies: List<Vacancy>?) {
        if (newVacancies != null) {
            setState { copy(
                isLoading = false,
                vacancies = if (isFirstLoad) {
                    newVacancies
                } else {
                    (viewState.vacancies ?: throw Exception("vacancies can't be null here")) + newVacancies
                },
                moreItemsState = if (newVacancies.size < DEFAULT_PAGE_SIZE) MoreItemsState.Unavailable else MoreItemsState.Available
            ) }
        } else {
            setState { copy(isLoading = false) }
        }
    }

    private fun loadVacancies(filters: VacancyFilters) {
        viewModelScope.launch {
            val isFirstLoad = filters.pageRequestFilter.pageNumber == START_PAGE

            if (isFirstLoad) {
                setState { copy(isLoading = true, filters = filters, pageNumber = START_PAGE, areRecommendationsShown = false) }
            } else {
                setState { copy(filters = filters, pageNumber = filters.pageRequestFilter.pageNumber, areRecommendationsShown = false) }
            }

            val vacanciesResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { vacancyRetrofit.getFilteredVacancies(filters.toVacancyFiltersDTO()).map { it.toDomainVacancy() } }
                )
            }

            resolvePostLoadState(isFirstLoad, vacanciesResult)
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

            val vacanciesResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { vacancyRetrofit.getVacancyRecommendations(
                        authToken = token.await() ?: throw Exception("token can't be null here"),
                        pageNumber = pageNumber
                    ) }
                )?.map { it.toDomainVacancy() }
            }

            resolvePostLoadState(isFirstLoad, vacanciesResult)
        }
    }

    companion object {
        fun provideFactory(openVacancyCallback: (String) -> Unit) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return WorkerHomeViewModel(openVacancyCallback) as T
                }
            }
    }
}