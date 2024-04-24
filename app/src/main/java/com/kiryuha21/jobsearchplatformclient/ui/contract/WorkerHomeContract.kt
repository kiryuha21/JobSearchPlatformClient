package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.domain.VacancyFilter
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

sealed class WorkerHomeContract {
    data class State(
        val isLoading: Boolean,
        val vacancies: List<Vacancy>?,
        val openedVacancy: Vacancy?,
        val filters: VacancyFilter
    ): ViewState

    sealed class Intent : ViewIntent {
        data class LoadVacancies(val filters: VacancyFilter): Intent()
        data class OpenVacancyDetails(val vacancyId: String): Intent()
    }
}