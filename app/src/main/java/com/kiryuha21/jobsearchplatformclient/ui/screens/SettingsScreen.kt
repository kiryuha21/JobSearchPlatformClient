package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.ui.components.AreYouSureDialogue
import com.kiryuha21.jobsearchplatformclient.ui.components.PasswordConfirmation
import com.kiryuha21.jobsearchplatformclient.ui.components.SecureConfirmAlertDialogue
import com.kiryuha21.jobsearchplatformclient.ui.components.SecuredFields
import com.kiryuha21.jobsearchplatformclient.ui.components.Title
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
            SecureConfirmAlertDialogue(
                title = "Подтвердите пароль для дальнейших действий",
                onConfirm = onPasswordDialogConfirmed,
                onDismiss = onPasswordDialogDismissed,
                isError = state.isPasswordError,
                isLoading = state.isDialogLoading
            )
        }
        if (state.isAreYouSureDialogShown) {
            AreYouSureDialogue(
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
        Button(onClick = onSaveChangesClicked) {
            Text(text = "Подтвердить изменения")
        }
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