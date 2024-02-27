package com.kiryuha21.jobsearchplatformclient.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.ui.components.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.SignUpForm
import com.kiryuha21.jobsearchplatformclient.ui.components.Title
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract

@Composable
fun SignUpScreen(
    state: AuthContract.AuthState,
    onRegister: () -> Unit
) {
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
        when (state.isLoading) {
            false -> SignUpForm(
                onRegister = onRegister,
                loginState = state.login,
                emailState = state.email,
                passwordState = state.password,
                passwordRepeatState = state.passwordRepeat,
                modifier = Modifier.fillMaxWidth()
            )

            true -> LoadingComponent()
        }

    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        AuthContract.AuthState(
            false,
            mutableStateOf(""),
            mutableStateOf(""),
            mutableStateOf(""),
            mutableStateOf("")
        ),
    ) {}
}