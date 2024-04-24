package com.kiryuha21.jobsearchplatformclient.ui.contract

import android.graphics.Bitmap
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

sealed class ResumeDetailsContract {
    data class State(
        val isLoading: Boolean,
        val openedResume: Resume?
    ) : ViewState

    sealed class Intent : ViewIntent {
        data class EditResume(val resume: Resume, val bitmap: Bitmap?) : Intent()
        data class CreateResume(val resume: Resume, val bitmap: Bitmap?) : Intent()
        data class DeleteResume(val resumeId: String) : Intent()
        data class LoadResume(val resumeId: String) : Intent()
        data class OpenEdit(val resume: Resume) : Intent()
    }
}