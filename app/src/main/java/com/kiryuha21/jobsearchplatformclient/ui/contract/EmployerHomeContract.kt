package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.ResumeFilters
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

sealed class EmployerHomeContract {
    data class State(
        val isLoading: Boolean,
        val resumes: List<Resume>?,
        val openedResume: Resume?,
        val filters: ResumeFilters
    ): ViewState

    sealed class Intent : ViewIntent {
        data class LoadResumes(val filters: ResumeFilters): Intent()
        data class OpenResumeDetails(val resumeId: String): Intent()
    }
}