package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.kiryuha21.jobsearchplatformclient.ui.contract.DialogContract

@Composable
fun SecureConfirmAlertDialogue(
    title: String,
    state: DialogContract.DialogState,
    onPasswordUpdate: (String) -> Unit,
    onConfirmRequest: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            Button(onClick = { onConfirmRequest(state.password) }) {
                Text(text = "Подтвердить")
            }
        },
        icon = {
            Icon(imageVector = Icons.Rounded.Security, contentDescription = "secure")
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
                    initString = state.password,
                    onUpdate = onPasswordUpdate
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun AreYouSureDialogue(
    icon: ImageVector,
    title: String,
    text: String,
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Подтвердить")
            }
        },
        icon = {
            Icon(imageVector = icon, contentDescription = "icon")
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = text)
        },
        modifier = modifier
    )
}