package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

sealed class WorkerProfileContract {
    data class State(
        val isLoading: Boolean,
        val resumes: List<Resume>?,
    ): ViewState

    sealed class Intent : ViewIntent {
        data object LoadResumes : Intent()
        data object CreateResume : Intent()
        data class OpenResumeDetails(val resumeId : String) : Intent()
    }
}