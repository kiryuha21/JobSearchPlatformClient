package com.kiryuha21.jobsearchplatformclient.ui.components.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.DefaultTextField
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.DefaultButton

@Composable
fun ResetPasswordForm(
    onReset: () -> Unit,
    onEmailFieldEdited: (String) -> Unit,
    initEmail: String,
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
            initString = initEmail,
            onUpdate = onEmailFieldEdited
        )
        DefaultButton(
            text = "Восстановить пароль",
            onClick = onReset,
            modifier = Modifier.padding(top = 20.dp)
        )
    }

}