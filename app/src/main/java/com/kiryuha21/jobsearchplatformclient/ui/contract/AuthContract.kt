package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

class AuthContract {
    sealed class AuthState: ViewState {
        object Loading: AuthState()
        object InternetError: AuthState()
        object OnLogIn: AuthState()
        object OnSignUp: AuthState()
        object OnResetPassword: AuthState()
    }

    sealed class AuthIntent: ViewIntent {
        object NavigateToLogin: AuthIntent()
        data class LogIn(val login: String, val password: String) : AuthIntent()

        object NavigateToResetPassword: AuthIntent()
        data class ResetPassword(val login: String) : AuthIntent()

        object NavigateToSignUp: AuthIntent()
        data class SignUp(val login: String, val password: String) : AuthIntent()
    }
}