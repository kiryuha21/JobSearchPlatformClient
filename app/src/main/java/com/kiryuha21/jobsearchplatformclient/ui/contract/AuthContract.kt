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
        data object LogIn : AuthIntent()

        data object NavigateToResetPassword: AuthIntent()
        data object ResetPassword : AuthIntent()

        data object NavigateToSignUp: AuthIntent()
        data object SignUp : AuthIntent()
    }
}