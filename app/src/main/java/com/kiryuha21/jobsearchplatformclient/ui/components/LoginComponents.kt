package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginForm(
    onLogin: () -> Unit,
    onResetPassword: () -> Unit,
    emailState: MutableState<String>,
    passwordState: MutableState<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultTextField(
            icon = Icons.Filled.Email,
            placeholder = "E-mail",
            textState = emailState,
            modifier = Modifier.padding(5.dp)
        )
        PasswordTextField(
            icon = Icons.Filled.Password,
            placeholder = "Пароль",
            textState = passwordState,
            modifier = Modifier.padding(5.dp)
        )
        ResetPasswordHelper(
            onClick = onResetPassword,
            modifier = Modifier.fillMaxWidth()
        )
        DefaultButton(
            text = "Войти",
            onClick = { onLogin() },
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
