package com.kiryuha21.jobsearchplatformclient.ui.components.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.SecuredPasswordTextField
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.SecuredTextField

@Composable
fun PasswordConfirmation(
    isVisible: Boolean,
    onConfirmClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = isVisible) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(start = 10.dp, end = 10.dp, top = 20.dp)
        ) {
            Text(text = "Подтвердите пароль для изменения данных")
            TextButton(onClick = onConfirmClicked) {
                Text(text = "Подтвердить")
            }
        }
    }
}

@Composable
fun SecuredFields(
    enabled: Boolean,
    onLoginFieldEdit: (String) -> Unit,
    onEmailFieldEdit: (String) -> Unit,
    onPasswordFieldEdit: (String) -> Unit,
    username: String,
    email: String,
    password: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        SecuredTextField(
            text = username,
            icon = Icons.Filled.Abc,
            enabled = enabled,
            placeholder = "my_login",
            label = "Новый логин",
            onUpdate = onLoginFieldEdit
        )
        SecuredTextField(
            text = email,
            icon = Icons.Filled.Email,
            enabled = enabled,
            label = "Новый e-mail",
            placeholder = "example@gmail.com",
            onUpdate = onEmailFieldEdit
        )
        SecuredPasswordTextField(
            text = password,
            icon = Icons.Filled.Password,
            enabled = enabled,
            placeholder = "",
            label = "Новый пароль",
            isError = false,
            supportingText = "",
            onUpdate = onPasswordFieldEdit
        )
    }
}