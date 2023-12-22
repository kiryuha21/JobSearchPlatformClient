package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResetPasswordForm(
    onReset: () -> Unit,
    emailState: MutableState<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        DefaultTextField(
            icon = Icons.Filled.Email,
            placeholder = "Ваша почта",
            textState = emailState
        )
        DefaultButton(
            text = "Восстановить пароль",
            onClick = onReset,
            modifier = Modifier.padding(top = 20.dp)
        )
    }

}