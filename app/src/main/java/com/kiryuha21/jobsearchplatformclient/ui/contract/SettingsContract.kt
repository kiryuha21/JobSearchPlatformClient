package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

class SettingsContract {
    data class SettingsState(
        val isLoading: Boolean,
        val password: String
    ): ViewState

    sealed class SettingsIntent: ViewIntent {
        data class ApplyDialog(val password: String): SettingsIntent()
        data class EditPassword(val newPassword: String): SettingsIntent()
    }
}