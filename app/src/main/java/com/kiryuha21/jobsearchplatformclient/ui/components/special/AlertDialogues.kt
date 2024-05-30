package com.kiryuha21.jobsearchplatformclient.ui.components.special

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.PasswordTextField

@Composable
fun SecureConfirmAlertDialog(
    title: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    isError: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { onConfirm(password) },
                    modifier = Modifier.fillMaxWidth(0.6f).testTag("settings_password_form_button")
                ) {
                    Text(text = "Подтвердить")
                }
            }
        },
        icon = {
            Icon(imageVector = Icons.Rounded.Security, contentDescription = "secure")
        },
        title = {
            Text(text = title, textAlign = TextAlign.Center)
        },
        text = {
            if (isLoading) {
                LoadingComponent(
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 20.dp),
                    description = "Проверка пароля..."
                )
            } else {
                Column {
                    Text(text = "Введите пароль для подтверждения")
                    PasswordTextField(
                        text = password,
                        icon = Icons.Rounded.Password,
                        placeholder = "Ваш пароль",
                        isError = isError,
                        supportingText = "Введен неверный пароль",
                        onUpdate = { password = it },
                        modifier = Modifier.testTag("settings_confirm_password")
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun AreYouSureDialog(
    title: String,
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    onClick = onConfirm,
                ) {
                    Text(text = "Внести изменения")
                }
            }
        },
        title = {
            Text(text = title, textAlign = TextAlign.Center)
        },
        text = {
            Text(text = "Нажмите вне диалога для отмены", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultDatePickerDialog(
    initTime: Long?,
    onDismiss: () -> Unit,
    onConfirm: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberDatePickerState(initialSelectedDateMillis = initTime)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { onConfirm(state.selectedDateMillis) }
                ) {
                    Text(text = "Выбрать")
                }
            }
        },
        modifier = modifier
    ) {
        DatePicker(state = state)
    }
}