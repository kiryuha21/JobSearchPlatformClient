package com.kiryuha21.jobsearchplatformclient.ui.contract

import android.graphics.Bitmap
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

sealed class ResumeDetailsContract {
    data class State(
        val isLoadingResume: Boolean,
        val isSavingResume: Boolean,
        val openedResume: Resume?,
        val loadingText: String
    ) : ViewState

    sealed interface Intent : ViewIntent {
        data class EditResume(val resume: Resume, val bitmap: Bitmap?) : Intent
        data class CreateResume(val resume: Resume, val bitmap: Bitmap?) : Intent
        data class LoadResume(val resumeId: String) : Intent
        data object OpenEdit : Intent
        data object DeleteResume : Intent
    }
}