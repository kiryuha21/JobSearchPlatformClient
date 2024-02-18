package com.kiryuha21.jobsearchplatformclient.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kiryuha21.jobsearchplatformclient.ui.components.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.SignUpForm
import com.kiryuha21.jobsearchplatformclient.ui.components.Title
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.UserDataStates
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

@Composable
fun SignUpScreen(viewState: State<ViewState>, userData: UserDataStates, onRegister: () -> Unit) {
    val state by viewState

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Title(
            text = "Регистрация",
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        )
        when (state) {
            AuthContract.AuthState.PageDefault -> SignUpForm(
                onRegister = onRegister,
                loginState = userData.login,
                emailState = userData.email,
                passwordState = userData.password,
                passwordRepeatState = userData.passwordRepeat,
                modifier = Modifier.fillMaxWidth()
            )

            is AuthContract.AuthState.Loading -> LoadingComponent()
        }

    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(mutableStateOf(AuthContract.AuthState.PageDefault) as State<ViewState>, UserDataStates()) {}
}