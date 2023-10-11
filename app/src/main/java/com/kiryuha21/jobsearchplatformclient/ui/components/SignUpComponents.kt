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
fun SignUpForm(onRegister: () -> Unit, modifier: Modifier = Modifier) {
    val placeholders = listOf("Имя", "Фамилия", "E-mail", "Пароль", "Повторите пароль")
    val icons = listOf(Icons.Filled.Abc, Icons.Filled.Abc, Icons.Filled.Email, Icons.Filled.Password, Icons.Filled.Password)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        (0..2).forEach {
            DefaultTextField(
                icon = icons[it],
                placeholder = placeholders[it],
                modifier = Modifier.padding(5.dp)
            )
        }
        (3..4).forEach {
            PasswordTextField(
                icon = icons[it],
                placeholder = placeholders[it],
                modifier = Modifier.padding(5.dp)
            )
        }
        DefaultButton(
            text = "Зарегистрироваться",
            onClick = { onRegister() },
            modifier = Modifier
                .padding(top = 20.dp)
                .defaultMinSize(minWidth = 200.dp)
        )
    }
}