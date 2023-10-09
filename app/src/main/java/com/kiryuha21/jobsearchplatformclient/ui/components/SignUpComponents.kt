package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun SignUpComponent() {
    Column {
        DefaultTextField(Icons.Filled.Abc, "Имя")
        DefaultTextField(Icons.Filled.Abc, "Фамилия")
        PasswordTextField(Icons.Filled.Password, "Пароль")
    }
}