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
        data class LogIn(val login: String, val password: String) : AuthIntent()

        data object NavigateToResetPassword: AuthIntent()
        data class ResetPassword(val login: String) : AuthIntent()

        data object NavigateToSignUp: AuthIntent()
        data class SignUp(val login: String, val password: String) : AuthIntent()
    }
}