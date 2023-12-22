package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kiryuha21.jobsearchplatformclient.ui.components.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.SignUpForm
import com.kiryuha21.jobsearchplatformclient.ui.components.Title
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(viewModel: AuthViewModel) {
    val state by viewModel.viewState

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Title(
            text = "Регистрация",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        )
        when (state) {
            AuthContract.AuthState.PageDefault -> SignUpForm(
                onRegister = {
                    viewModel.processIntent(
                        AuthContract.AuthIntent.SignUp(
                            viewModel.email.value,
                            viewModel.password.value
                        )
                    )
                },
                nameState = viewModel.name,
                surnameState = viewModel.surname,
                emailState = viewModel.email,
                passwordState = viewModel.password,
                passwordRepeatState = viewModel.passwordRepeat,
                modifier = Modifier.fillMaxWidth()
            )

            is AuthContract.AuthState.Loading -> LoadingComponent()
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(viewModel = viewModel())
}