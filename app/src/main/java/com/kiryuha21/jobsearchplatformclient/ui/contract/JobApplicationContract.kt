package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.data.domain.JobApplication
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

sealed class JobApplicationContract {
    data class State(
        val isLoading: Boolean,
        val sentApplications: List<JobApplication>,
        val receivedApplications: List<JobApplication>
    ): ViewState

    sealed interface Intent : ViewIntent {
        data object LoadApplications : Intent
        data class ShowVacancyDetails(val vacancyId: String): Intent
        data class MarkSeen(val jobApplicationId: String): Intent
    }
}