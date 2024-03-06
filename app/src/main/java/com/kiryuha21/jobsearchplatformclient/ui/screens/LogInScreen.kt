package com.kiryuha21.jobsearchplatformclient.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.R
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.ui.components.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.LoginForm
import com.kiryuha21.jobsearchplatformclient.ui.components.NotSignedUpHelper
import com.kiryuha21.jobsearchplatformclient.ui.components.Title
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract

@Composable
fun LogInScreen(
    state: AuthContract.AuthState,
    onEmailFieldEdited: (String) -> Unit,
    onPasswordFieldEdited: (String) -> Unit,
    onLogin: () -> Unit,
    onResetPassword: () -> Unit,
    onSignUp: () -> Unit
) {
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
        when (state.isLoading) {
            true -> LoadingComponent()

            false -> {
                LoginForm(
                    onLogin = onLogin,
                    onResetPassword = onResetPassword,
                    onEmailFieldEdited = onEmailFieldEdited,
                    onPasswordFieldEdited = onPasswordFieldEdited,
                    initEmail = state.email,
                    initPassword = state.password
                )
                NotSignedUpHelper(
                    onClick = onSignUp,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginScreenPreview() {
    LogInScreen(
        AuthContract.AuthState(
            isLoading = false,
            isError = false,
            email = "",
            login = "",
            role = UserRole.Worker,
            password ="",
            passwordRepeat = ""
        ),
        {},
        {},
        {},
        {},
        {})
}