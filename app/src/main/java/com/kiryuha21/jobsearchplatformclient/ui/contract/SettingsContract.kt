package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

enum class ImportantAction {
    DeleteAccount,
    ChangeAccountData
}

sealed class SettingsContract {
    data class State(
        val isScreenLoading: Boolean,
        val isDialogLoading: Boolean,
        val isPasswordDialogShown: Boolean,
        val isAreYouSureDialogShown: Boolean,
        val isPasswordError: Boolean,
        val areFieldsEnabled: Boolean,
        val importantAction: ImportantAction,
        val areNotificationsOn: Boolean,
        val username: String,
        val email: String,
        val password: String
    ): ViewState

    sealed interface Intent: ViewIntent {
        data class EditPassword(val newPassword: String): Intent
        data class EditUsername(val newUsername: String): Intent
        data class EditEmail(val newEmail: String): Intent
        data class CheckPassword(val password: String) : Intent
        data class ShowAreYouSureDialog(val action: ImportantAction) : Intent
        data class ToggleNotifications(val newValue: Boolean) : Intent
        data object CommitChanges : Intent
        data object ShowPasswordDialog : Intent
        data object HidePasswordDialog : Intent
        data object HideAreYouSureDialog : Intent

    }
}