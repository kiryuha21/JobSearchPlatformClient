package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainVacancy
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitObject.vacancyRetrofit
import com.kiryuha21.jobsearchplatformclient.ui.contract.EmployerProfileContract
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmployerProfileViewModel(
    private val createVacancyCallback: () -> Unit,
    private val openVacancyDetailsCallback: (String) -> Unit
): BaseViewModel<EmployerProfileContract.Intent, EmployerProfileContract.State>() {
    override fun initialState(): EmployerProfileContract.State {
        return EmployerProfileContract.State(
            isLoading = true,
            vacancies = null
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is EmployerProfileContract.Intent.LoadVacancies -> loadProfileVacancies()
            is EmployerProfileContract.Intent.OpenVacancyForm -> createVacancyCallback()
            is EmployerProfileContract.Intent.OpenVacancyDetails -> openVacancyDetailsCallback(intent.vacancyId)
        }
    }

    private fun loadProfileVacancies() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val vacanciesResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { vacancyRetrofit.getVacanciesByEmployerLogin(CurrentUser.info.username).map { it.toDomainVacancy() } }
                )
            }
            setState { copy(isLoading = false, vacancies = vacanciesResult) }
        }
    }

    companion object {
        fun provideFactory(
            openVacancyDetailsCallback: (String) -> Unit,
            createVacancyCallback: () -> Unit
        ) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return EmployerProfileViewModel(
                        createVacancyCallback = createVacancyCallback,
                        openVacancyDetailsCallback = openVacancyDetailsCallback
                    ) as T
                }
            }
    }
}