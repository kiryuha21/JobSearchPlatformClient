package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

sealed class AuthContract {
    data class AuthState(
        val isLoading: Boolean,
        val isError: Boolean,
        val email: String,
        val username: String,
        val role: UserRole,
        val password: String,
        val passwordRepeat: String
    ): ViewState

    sealed interface AuthIntent: ViewIntent {
        data object NavigateToLogin: AuthIntent
        data object LogIn : AuthIntent

        data object NavigateToResetPassword: AuthIntent
        data object ResetPassword : AuthIntent

        data object NavigateToSignUp: AuthIntent
        data object SignUp : AuthIntent

        data class EditLogin(val newLogin: String) : AuthIntent
        data class EditEmail(val newEmail: String) : AuthIntent
        data class EditPassword(val newPassword: String) : AuthIntent
        data class EditPasswordRepeat(val newPasswordRepeat: String) : AuthIntent
        data class EditRole(val newRole: UserRole): AuthIntent

        data object FixError : AuthIntent
        data object CheckRefreshToken : AuthIntent
    }
}