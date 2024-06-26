package com.kiryuha21.jobsearchplatformclient.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.ui.components.auth.SignUpForm
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.FixableErrorComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.Title
import com.kiryuha21.jobsearchplatformclient.ui.components.special.ToggleButtonElement
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract

@Composable
fun SignUpScreen(
    state: AuthContract.AuthState,
    onLoginFieldUpdated: (String) -> Unit,
    onEmailFieldUpdated: (String) -> Unit,
    onPasswordFieldUpdated: (String) -> Unit,
    onPasswordRepeatFieldUpdated: (String) -> Unit,
    onRoleToggled: (ToggleButtonElement) -> Unit,
    onErrorFix: () -> Unit,
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
        when {
            state.isLoading -> LoadingComponent(
                modifier = Modifier.fillMaxWidth()
            )
            state.isError -> FixableErrorComponent(
                errorImage = Icons.Rounded.Error,
                text = "Ой! Что-то пошло не по плану",
                fixImage = Icons.Rounded.Refresh,
                fixFunction = onErrorFix,
                modifier = Modifier.fillMaxWidth()
            )
            else -> {
                SignUpForm(
                    onRegister = onRegister,
                    onLoginFieldUpdated = onLoginFieldUpdated,
                    onEmailFieldUpdated = onEmailFieldUpdated,
                    onPasswordFieldUpdated = onPasswordFieldUpdated,
                    onPasswordRepeatFieldUpdated = onPasswordRepeatFieldUpdated,
                    onRoleToggled = onRoleToggled,
                    state = state,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        AuthContract.AuthState(
            isLoading = false,
            isError = false,
            email = "",
            username = "",
            role = UserRole.Worker,
            password ="",
            passwordRepeat = ""
        ),
        {}, {}, {}, {}, {}, {}
    ) {}
}