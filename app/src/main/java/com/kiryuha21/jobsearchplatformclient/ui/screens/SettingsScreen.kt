package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.SecuredButton
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.Title
import com.kiryuha21.jobsearchplatformclient.ui.components.settings.PasswordConfirmation
import com.kiryuha21.jobsearchplatformclient.ui.components.settings.SecuredFields
import com.kiryuha21.jobsearchplatformclient.ui.components.special.AreYouSureDialog
import com.kiryuha21.jobsearchplatformclient.ui.components.special.SecureConfirmAlertDialog
import com.kiryuha21.jobsearchplatformclient.ui.contract.SettingsContract

@Composable
fun SettingsScreen(
    state: SettingsContract.State,
    onUsernameFieldEdited: (String) -> Unit,
    onEmailFieldEdited: (String) -> Unit,
    onPasswordFieldEdited: (String) -> Unit,
    onShowPasswordDialog: () -> Unit,
    onPasswordDialogDismissed: () -> Unit,
    onPasswordDialogConfirmed: (String) -> Unit,
    onSaveChangesClicked: () -> Unit,
    onAreYouSureDialogConfirmed: () -> Unit,
    onAreYouSureDialogDismissed: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        if (state.isPasswordDialogShown) {
            SecureConfirmAlertDialog(
                title = "Подтвердите пароль для дальнейших действий",
                onConfirm = onPasswordDialogConfirmed,
                onDismiss = onPasswordDialogDismissed,
                isError = state.isPasswordError,
                isLoading = state.isDialogLoading
            )
        }
        if (state.isAreYouSureDialogShown) {
            AreYouSureDialog(
                title = "Вы уверены, что хотите изменить данные?",
                onConfirm = onAreYouSureDialogConfirmed,
                onDismiss = onAreYouSureDialogDismissed
            )
        }
        Title(text = "Настройки", fontSize = 30.sp)
        PasswordConfirmation(
            modifier = Modifier.fillMaxWidth(),
            onConfirmClicked = onShowPasswordDialog
        )
        SecuredFields(
            enabled = state.areFieldsEnabled,
            onLoginFieldEdit = onUsernameFieldEdited,
            onEmailFieldEdit = onEmailFieldEdited,
            onPasswordFieldEdit = onPasswordFieldEdited,
            initUsername = state.username,
            initEmail = state.email,
            initPassword = state.password
        )
        SecuredButton(
            text = "Подтвердить изменения",
            onClick = onSaveChangesClicked,
            enabled = state.areFieldsEnabled
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsScreenPreview() {
    SettingsScreen(
        SettingsContract.State(
            isScreenLoading = false,
            isDialogLoading = false,
            password = "",
            areFieldsEnabled = false,
            isPasswordError = false,
            username = "",
            email = "",
            isAreYouSureDialogShown = false,
            isPasswordDialogShown = false
        ), {}, {}, {}, {}, {}, {}, {}, {}, {})
}