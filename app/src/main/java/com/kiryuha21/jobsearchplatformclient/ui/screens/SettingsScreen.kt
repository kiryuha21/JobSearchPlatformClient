package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.ui.components.PasswordConfirmation
import com.kiryuha21.jobsearchplatformclient.ui.components.SecuredFields
import com.kiryuha21.jobsearchplatformclient.ui.components.Title

@Composable
fun SettingsScreen(
    onLoginFieldEdited: (String) -> Unit,
    onEmailFieldEdited: (String) -> Unit,
    onPasswordFieldEdited: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Title(text = "Настройки", fontSize = 30.sp)
            PasswordConfirmation(modifier = Modifier.fillMaxWidth()) {
                // TODO
            }
            SecuredFields(
                enabled = false,
                onLoginFieldEdit = onLoginFieldEdited,
                onEmailFieldEdit = onEmailFieldEdited,
                onPasswordFieldEdit = onPasswordFieldEdited
            )
        }

    }
}