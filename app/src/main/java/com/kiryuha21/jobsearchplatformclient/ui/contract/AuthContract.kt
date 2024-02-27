package com.kiryuha21.jobsearchplatformclient.ui.contract

import androidx.compose.runtime.MutableState
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

class AuthContract {
    data class AuthState(
        val isLoading: Boolean,
        val email: String,
        val login: String,
        val password: String,
        val passwordRepeat: String
    ): ViewState

    sealed class AuthIntent: ViewIntent {
        data object NavigateToLogin: AuthIntent()
        data object LogIn : AuthIntent()

        data object NavigateToResetPassword: AuthIntent()
        data object ResetPassword : AuthIntent()

        data object NavigateToSignUp: AuthIntent()
        data object SignUp : AuthIntent()

        data class EditLogin(val newLogin: String) : AuthIntent()
        data class EditEmail(val newEmail: String) : AuthIntent()
        data class EditPassword(val newPassword: String) : AuthIntent()
        data class EditPasswordRepeat(val newPasswordRepeat: String) : AuthIntent()
    }
}