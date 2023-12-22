package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SignUpForm(
    onRegister: () -> Unit,
    nameState: MutableState<String>,
    surnameState: MutableState<String>,
    emailState: MutableState<String>,
    passwordState: MutableState<String>,
    passwordRepeatState: MutableState<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultTextField(
            icon = Icons.Filled.Abc,
            placeholder = "Имя",
            textState = nameState,
            modifier = Modifier.padding(5.dp)
        )
        DefaultTextField(
            icon = Icons.Filled.Abc,
            placeholder = "Фамилия",
            textState = surnameState,
            modifier = Modifier.padding(5.dp)
        )
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
        PasswordTextField(
            icon = Icons.Filled.Password,
            placeholder = "Повторите пароль",
            textState = passwordRepeatState,
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