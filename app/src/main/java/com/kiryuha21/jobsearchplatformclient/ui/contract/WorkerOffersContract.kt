package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.data.domain.JobApplication
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

sealed class WorkerOffersContract {
    data class State(
        val isLoading: Boolean,
        val jobApplications: List<JobApplication>
    ): ViewState

    sealed interface Intent : ViewIntent {
        data object LoadOffers : Intent
        data class ShowVacancyDetails(val vacancyId: String): Intent
        data class MarkSeen(val jobApplicationId: String): Intent
    }
}