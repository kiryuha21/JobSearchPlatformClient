package com.kiryuha21.jobsearchplatformclient.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kiryuha21.jobsearchplatformclient.R
import com.kiryuha21.jobsearchplatformclient.ui.components.ErrorComponent
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
        Title(
            text = "Платформа для поиска работы",
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

            is AuthContract.AuthState.InternetError -> Toast.makeText(
                LocalContext.current,
                R.string.no_internet_connection,
                Toast.LENGTH_SHORT
            ).show()

            else -> ErrorComponent(
                image = Icons.Filled.Error,
                text = "State machine error",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginScreenPreview() {
    LogInScreen(viewModel = viewModel())
}