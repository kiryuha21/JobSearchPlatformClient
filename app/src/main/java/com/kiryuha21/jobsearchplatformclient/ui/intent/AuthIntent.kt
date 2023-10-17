package com.kiryuha21.jobsearchplatformclient.ui.intent

sealed class AuthIntent {
    //object LoadLoginScreen: AuthIntent()
    data class LogIn(val login: String, val password: String) : AuthIntent()

    //object LoadResetPasswordScreen: AuthIntent()
    data class ResetPassword(val login: String) : AuthIntent()

    //object LoadSignUpScreen: AuthIntent()
    data class SignUp(val login: String, val password: String) : AuthIntent()
}