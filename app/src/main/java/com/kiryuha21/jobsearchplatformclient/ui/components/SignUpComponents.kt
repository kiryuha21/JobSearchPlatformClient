package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SignUpForm(
    onRegister: () -> Unit,
    onLoginFieldUpdated: (String) -> Unit,
    onEmailFieldUpdated: (String) -> Unit,
    onPasswordFieldUpdated: (String) -> Unit,
    onPasswordRepeatFieldUpdated: (String) -> Unit,
    initLogin: String,
    initEmail: String,
    initPassword: String,
    initPasswordRepeat: String,
    passwordRepeatNotMatches: Boolean,
    passwordRepeatSupportingText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultTextField(
            icon = Icons.Filled.Abc,
            placeholder = "Логин",
            onUpdate = onLoginFieldUpdated,
            initString = initLogin,
            modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
        )
        DefaultTextField(
            icon = Icons.Filled.Email,
            placeholder = "E-mail",
            onUpdate = onEmailFieldUpdated,
            initString = initEmail,
            modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 15.dp, bottom = 10.dp)
        )
        PasswordTextField(
            icon = Icons.Filled.Password,
            placeholder = "Пароль",
            onUpdate = onPasswordFieldUpdated,
            initString = initPassword,
            isError = false,
            supportingText = "",
            modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 15.dp, bottom = 5.dp)
        )
        PasswordTextField(
            icon = Icons.Filled.Password,
            placeholder = "Повторите пароль",
            onUpdate = onPasswordRepeatFieldUpdated,
            initString = initPasswordRepeat,
            isError = passwordRepeatNotMatches,
            supportingText = passwordRepeatSupportingText,
            modifier = Modifier.padding(5.dp)
        )
        DefaultButton(
            text = "Зарегистрироваться",
            onClick = { onRegister() },
            modifier = Modifier
                .padding(top = 20.dp)
                .defaultMinSize(minWidth = 200.dp)
        )
    }
}