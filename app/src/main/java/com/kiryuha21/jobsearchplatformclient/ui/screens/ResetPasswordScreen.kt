package com.kiryuha21.jobsearchplatformclient.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.ui.components.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.ResetPasswordForm
import com.kiryuha21.jobsearchplatformclient.ui.components.Title
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract

@Composable
fun ResetPasswordScreen(
    state: AuthContract.AuthState,
    onEmailFieldEdited: (String) -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Title(
            text = "Восстановление пароля",
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        )
        when (state.isLoading) {
            false -> ResetPasswordForm(
                onReset = onReset,
                onEmailFieldEdited = onEmailFieldEdited,
                initEmail = state.email,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            )

            true -> LoadingComponent()
        }

    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ResetPasswordScreenPreview() {
    ResetPasswordScreen(
        AuthContract.AuthState(
            isLoading = false,
            isError = false,
            email = "",
            login = "",
            password ="",
            passwordRepeat = ""
        ), {}
    ) {}
}