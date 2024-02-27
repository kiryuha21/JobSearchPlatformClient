package com.kiryuha21.jobsearchplatformclient.ui.contract

import androidx.compose.runtime.MutableState
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

class AuthContract {
    data class AuthState(
        val isLoading: Boolean,
        val email: MutableState<String>,
        val login: MutableState<String>,
        val password: MutableState<String>,
        val passwordRepeat: MutableState<String>
    ): ViewState

    sealed class AuthIntent: ViewIntent {
        data object NavigateToLogin: AuthIntent()
        data object LogIn : AuthIntent()

        data object NavigateToResetPassword: AuthIntent()
        data object ResetPassword : AuthIntent()

        data object NavigateToSignUp: AuthIntent()
        data object SignUp : AuthIntent()
    }
}