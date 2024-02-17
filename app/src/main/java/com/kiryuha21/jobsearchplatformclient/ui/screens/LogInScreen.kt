package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kiryuha21.jobsearchplatformclient.R
import com.kiryuha21.jobsearchplatformclient.ui.components.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.LoginForm
import com.kiryuha21.jobsearchplatformclient.ui.components.NotSignedUpHelper
import com.kiryuha21.jobsearchplatformclient.ui.components.Title
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel

@Composable
fun LogInScreen(viewModel: AuthViewModel) {
    val state by viewModel.viewState

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight(0.4f)
                .fillMaxWidth()
        ) {
            val context = LocalContext.current
            Title(
                text = context.getString(R.string.app_name),
                fontSize = 28.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Title(
                text = context.getString(R.string.subtitle),
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
        when (state) {
            is AuthContract.AuthState.PageDefault -> {
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
                    },
                    emailState = viewModel.email,
                    passwordState = viewModel.password,
                )
                NotSignedUpHelper(
                    onClick = { viewModel.processIntent(AuthContract.AuthIntent.NavigateToSignUp) },
                    modifier = Modifier.fillMaxHeight()
                )
            }

            is AuthContract.AuthState.Loading -> LoadingComponent()
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginScreenPreview() {
    LogInScreen(viewModel = viewModel())
}