package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.SecuredButton
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.Title
import com.kiryuha21.jobsearchplatformclient.ui.components.settings.PasswordConfirmation
import com.kiryuha21.jobsearchplatformclient.ui.components.settings.SecuredFields
import com.kiryuha21.jobsearchplatformclient.ui.components.special.AreYouSureDialog
import com.kiryuha21.jobsearchplatformclient.ui.components.special.SecureConfirmAlertDialog
import com.kiryuha21.jobsearchplatformclient.ui.contract.ImportantAction
import com.kiryuha21.jobsearchplatformclient.ui.contract.SettingsContract

@Composable
fun SettingsScreen(
    state: SettingsContract.State,
    onUsernameFieldEdited: (String) -> Unit,
    onEmailFieldEdited: (String) -> Unit,
    onPasswordFieldEdited: (String) -> Unit,
    showAreYouSureDialog: (ImportantAction) -> Unit,
    onShowPasswordDialog: () -> Unit,
    onPasswordDialogDismissed: () -> Unit,
    onPasswordDialogConfirmed: (String) -> Unit,
    onAreYouSureDialogConfirmed: () -> Unit,
    onAreYouSureDialogDismissed: () -> Unit,
    toggleNotifications: (Boolean) -> Unit
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
                title = if (state.importantAction == ImportantAction.ChangeAccountData) {
                    "Вы уверены, что хотите изменить данные?"
                } else {
                    "Вы уверены, что хотите удалить аккаунт?"
                },
                onConfirm = onAreYouSureDialogConfirmed,
                onDismiss = onAreYouSureDialogDismissed
            )
        }

        Title(text = "Настройки", fontSize = 30.sp)
        if (state.isScreenLoading) {
            LoadingComponent(modifier = Modifier.fillMaxSize())
        } else {
            PasswordConfirmation(
                isVisible = !state.areFieldsEnabled,
                onConfirmClicked = onShowPasswordDialog,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Данные аккаунта",
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp)
            )
            SecuredFields(
                enabled = state.areFieldsEnabled,
                onLoginFieldEdit = onUsernameFieldEdited,
                onEmailFieldEdit = onEmailFieldEdited,
                onPasswordFieldEdit = onPasswordFieldEdited,
                username = state.username,
                email = state.email,
                password = state.password
            )

            HorizontalDivider()

            Text(
                text = "Уведомления",
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .padding(top = 10.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            ) {
                Text(
                    text = "Новые предложения работы",
                    fontSize = 18.sp
                )
                Switch(
                    checked = state.areNotificationsOn,
                    onCheckedChange = { toggleNotifications(it) },
                    enabled = state.areFieldsEnabled
                )
            }

            HorizontalDivider()

            Text(
                text = "Аккаунт",
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .padding(top = 10.dp)
            )

            Button(
                onClick = { showAreYouSureDialog(ImportantAction.DeleteAccount) },
                enabled = state.areFieldsEnabled,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            ) {
                Text(text = "Удалить аккаунт")
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                SecuredButton(
                    text = "Подтвердить изменения",
                    onClick = { showAreYouSureDialog(ImportantAction.ChangeAccountData) },
                    enabled = state.areFieldsEnabled,
                    modifier = Modifier.padding(10.dp)
                )
            }
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
            isPasswordDialogShown = false,
            importantAction = ImportantAction.DeleteAccount,
            areNotificationsOn = true
        ), {}, {}, {}, {}, {}, {}, {}, {}, {}, {})
}