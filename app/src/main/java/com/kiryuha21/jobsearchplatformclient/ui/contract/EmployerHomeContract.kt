package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.ResumeFilter
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

sealed class EmployerHomeContract {
    data class State(
        val isLoading: Boolean,
        val resumes: List<Resume>?,
        val openedResume: Resume?,
        val filters: ResumeFilter
    ): ViewState

    sealed class Intent : ViewIntent {
        data class LoadResumes(val filters: ResumeFilter): Intent()
        data class OpenResumeDetails(val resumeId: String): Intent()
    }
}