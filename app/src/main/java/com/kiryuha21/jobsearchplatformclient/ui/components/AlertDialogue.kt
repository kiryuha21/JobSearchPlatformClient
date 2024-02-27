package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kiryuha21.jobsearchplatformclient.ui.contract.DialogContract

@Composable
fun SecureConfirmAlertDialogue(
    title: String,
    state: DialogContract.DialogState,
    onConfirmRequest: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            Button(onClick = { onConfirmRequest(state.passwordState.value) }) {
                Text(text = "Подтвердить")
            }
        },
        title = {
            Text(text = title)
        },
        text = {
               Column {
                   Text(text = "Введите пароль для подтверждения")
                   PasswordTextField(
                       icon = Icons.Rounded.Password,
                       placeholder = "Ваш пароль",
                       textState = state.passwordState
                   )
               }
        },
        modifier = modifier
    )
}