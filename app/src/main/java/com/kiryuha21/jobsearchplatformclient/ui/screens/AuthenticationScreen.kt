package com.kiryuha21.jobsearchplatformclient.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.kiryuha21.jobsearchplatformclient.R
import com.kiryuha21.jobsearchplatformclient.ui.components.BackHandlerWithWarning
import com.kiryuha21.jobsearchplatformclient.ui.components.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.LoginForm
import com.kiryuha21.jobsearchplatformclient.ui.components.NotSignedUpHelper
import com.kiryuha21.jobsearchplatformclient.ui.components.ResetPasswordForm
import com.kiryuha21.jobsearchplatformclient.ui.components.SignUpForm
import com.kiryuha21.jobsearchplatformclient.ui.components.Title
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel

@Composable
fun AuthenticationScreen(viewModel: AuthViewModel) {
    val state by viewModel.viewState
    BackHandlerWithWarning(message = "Нажмите снова для выхода", msDelay = 2000)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Title(
            text = when(state) {
                is AuthContract.AuthState.OnSignUp -> "Регистрация"
                is AuthContract.AuthState.OnResetPassword -> "Восстановление пароля"
                else -> "Платформа для поиска работы"
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
        )
        when (state) {
            is AuthContract.AuthState.OnLogIn -> {
                LoginForm(
                    onLogin = {
                        viewModel.processIntent(
                            AuthContract.AuthIntent.LogIn(
                                viewModel.email.value,
                                viewModel.password.value
                            )
                        )
                    },
                    onResetPassword = {
                        viewModel.processIntent(AuthContract.AuthIntent.NavigateToResetPassword)
                    })
                NotSignedUpHelper(
                    onClick = { viewModel.processIntent(AuthContract.AuthIntent.NavigateToSignUp) },
                    modifier = Modifier.fillMaxHeight()
                )
            }

            is AuthContract.AuthState.OnSignUp -> {
                SignUpForm(
                    onRegister = {
                        viewModel.processIntent(
                            AuthContract.AuthIntent.SignUp(
                                viewModel.email.value,
                                viewModel.password.value
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            is AuthContract.AuthState.OnResetPassword -> {
                ResetPasswordForm(
                    onReset = {
                        viewModel.processIntent(
                            AuthContract.AuthIntent.ResetPassword(viewModel.email.value)
                        )
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                )
            }

            is AuthContract.AuthState.InternetError -> {
                Toast.makeText(
                    LocalContext.current,
                    R.string.no_internet_connection,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.processIntent(AuthContract.AuthIntent.NavigateToLogin)
            }

            AuthContract.AuthState.Loading -> LoadingComponent()
        }
    }
}