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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kiryuha21.jobsearchplatformclient.R
import com.kiryuha21.jobsearchplatformclient.ui.components.ErrorComponent
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
                modifier = Modifier.fillMaxWidth()
            )

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
fun SignUpScreenPreview() {
    SignUpScreen(viewModel = viewModel())
}