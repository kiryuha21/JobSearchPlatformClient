package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser

@Composable
fun PasswordConfirmation(
    modifier: Modifier = Modifier,
    onConfirmClicked: () -> Unit
) {
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

@Composable
fun SecuredFields(
    enabled: Boolean,
    onLoginFieldEdit: (String) -> Unit,
    onEmailFieldEdit: (String) -> Unit,
    onPasswordFieldEdit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        SecuredTextField(
            icon = Icons.Filled.Abc,
            enabled = enabled,
            placeholder = "my_login",
            initString = CurrentUser.userInfo.value.login,
            onUpdate = onLoginFieldEdit
        )
        SecuredTextField(
            icon = Icons.Filled.Email,
            enabled = enabled,
            placeholder = "example@gmail.com",
            initString = CurrentUser.userInfo.value.email,
            onUpdate = onEmailFieldEdit
        )
        SecuredPasswordTextField(
            icon = Icons.Filled.Password,
            enabled = enabled,
            placeholder = "",
            initString = "",
            onUpdate = onPasswordFieldEdit
        )
    }
}