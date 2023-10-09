package com.kiryuha21.jobsearchplatformclient.ui.viewstate

sealed class AuthState {
    object Loading: AuthState()
    object OnLogIn: AuthState()
    object OnSignUp: AuthState()
    object OnResetPassword: AuthState()
}