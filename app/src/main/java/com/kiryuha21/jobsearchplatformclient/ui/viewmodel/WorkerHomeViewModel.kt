package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.VacancyFilters
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainVacancy
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitObject.vacancyRetrofit
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerHomeContract
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkerHomeViewModel(
    private val openVacancyCallback: (String) -> Unit
) : BaseViewModel<WorkerHomeContract.Intent, WorkerHomeContract.State>() {
    override fun initialState(): WorkerHomeContract.State {
        return WorkerHomeContract.State(
            isLoading = true,
            vacancies = null,
            openedVacancy = null,
            filters = VacancyFilters()
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is WorkerHomeContract.Intent.LoadVacancies -> loadVacancies(intent.filters)
            is WorkerHomeContract.Intent.OpenVacancyDetails -> openVacancyDetails(intent.vacancyId)
        }
    }

    private fun loadVacancies(filter: VacancyFilters) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val vacanciesResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { vacancyRetrofit.getMatchingVacancies().map { it.toDomainVacancy() } }
                )
            }
            setState { copy(isLoading = false, vacancies = vacanciesResult) }
        }
    }

    private fun openVacancyDetails(vacancyId: String) {
        openVacancyCallback(vacancyId)
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