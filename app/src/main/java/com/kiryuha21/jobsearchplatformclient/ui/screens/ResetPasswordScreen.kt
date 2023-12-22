package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kiryuha21.jobsearchplatformclient.ui.components.ErrorComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.ResetPasswordForm
import com.kiryuha21.jobsearchplatformclient.ui.components.Title
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel

@Composable
fun ResetPasswordScreen(viewModel: AuthViewModel) {
    val state by viewModel.viewState

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Title(
            text = "Восстановление пароля",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        )
        when (state) {
            AuthContract.AuthState.PageDefault -> ResetPasswordForm(
                onReset = {
                    viewModel.processIntent(
                        AuthContract.AuthIntent.ResetPassword(viewModel.email.value)
                    )
                },
                emailState = viewModel.email,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            )

            else -> ErrorComponent(
                image = Icons.Filled.Error,
                text = "State machine error",
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun resetPasswordScreenPreview() {
    ResetPasswordScreen(viewModel = viewModel())
}