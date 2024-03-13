package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun LoginForm(
    onLogin: () -> Unit,
    onResetPassword: () -> Unit,
    onUsernameFieldEdited: (String) -> Unit,
    onPasswordFieldEdited: (String) -> Unit,
    initUsername: String,
    initPassword: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultTextField(
            icon = Icons.Filled.Abc,
            placeholder = "Логин",
            onUpdate = onUsernameFieldEdited,
            initString = initUsername,
            modifier = Modifier.padding(5.dp).testTag("login_username")
        )
        PasswordTextField(
            icon = Icons.Filled.Password,
            placeholder = "Пароль",
            onUpdate = onPasswordFieldEdited,
            initString = initPassword,
            isError = false,
            supportingText = "",
            modifier = Modifier.padding(5.dp).testTag("login_password")
        )
        ResetPasswordHelper(
            onClick = onResetPassword,
            modifier = Modifier.fillMaxWidth()
        )
        DefaultButton(
            text = "Войти",
            onClick = onLogin,
            modifier = Modifier
                .padding(top = 20.dp)
                .defaultMinSize(minWidth = 200.dp)
        )
    }
}

@Composable
fun NotSignedUpHelper(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Не зарегистрированы?"
        )
        FramelessButton(
            text = "Зарегистрироваться",
            onClick = onClick
        )
    }
}

@Composable
fun ResetPasswordHelper(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Забыли пароль?"
        )
        FramelessButton(
            text = "Восстановить пароль",
            onClick = onClick
        )
    }
}
