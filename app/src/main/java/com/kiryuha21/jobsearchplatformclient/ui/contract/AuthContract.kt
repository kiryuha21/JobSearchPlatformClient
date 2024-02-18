package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

class AuthContract {
    sealed class AuthState: ViewState {
        data object Loading: AuthState()
        data object PageDefault: AuthState()
    }

    sealed class AuthIntent: ViewIntent {
        data object NavigateToLogin: AuthIntent()
        data class LogIn(val userDelegate: (UserContract.UserIntent.TryLogIn) -> Unit) : AuthIntent()

        data object NavigateToResetPassword: AuthIntent()
        data class ResetPassword(val userDelegate: (UserContract.UserIntent.TryResetPassword) -> Unit) : AuthIntent()

        data object NavigateToSignUp: AuthIntent()
        data class SignUp(val userDelegate: (UserContract.UserIntent.TrySignUp) -> Unit) : AuthIntent()
    }
}