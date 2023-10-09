package com.kiryuha21.jobsearchplatformclient.ui.intent

sealed class AuthIntent {
    data class ResetPassword(val login: String): AuthIntent()
    data class LogIn(val login: String, val password: String): AuthIntent()
    data class SignUp(val login: String, val password: String): AuthIntent()
}