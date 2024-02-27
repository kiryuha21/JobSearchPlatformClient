package com.kiryuha21.jobsearchplatformclient.ui.contract

import androidx.compose.runtime.MutableState
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

class DialogContract {
    data class DialogState(
        val isLoading: Boolean,
        val passwordState: MutableState<String>
    ): ViewState

    sealed class DialogIntent: ViewIntent {
        data class ApplyDialog(val password: String): DialogIntent()
    }
}