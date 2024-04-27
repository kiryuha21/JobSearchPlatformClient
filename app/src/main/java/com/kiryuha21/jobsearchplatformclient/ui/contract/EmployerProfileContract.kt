package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

class EmployerProfileContract {
    data class State(
        val isLoading: Boolean,
        val vacancies: List<Vacancy>?,
    ): ViewState

    sealed class Intent : ViewIntent {
        data object LoadVacancies: Intent()
        data object CreateVacancy: Intent()
        data class OpenVacancyDetails(val vacancyId: String): Intent()
    }
}