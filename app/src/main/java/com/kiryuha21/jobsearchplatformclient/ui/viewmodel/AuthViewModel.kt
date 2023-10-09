package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.kiryuha21.jobsearchplatformclient.ui.intent.AuthIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewstate.AuthState

class AuthViewModel : ViewModel() {
    private val _state = mutableStateOf<AuthState>(AuthState.OnLogIn)
    val state: State<AuthState> = _state

    fun processIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.LogIn -> tryLogIn(intent.login, intent.password)
            is AuthIntent.SignUp -> createNewUser(intent.login, intent.password)
            is AuthIntent.ResetPassword -> resetPassword(intent.login)
        }
    }

    private fun tryLogIn(login: String, password: String) {

    }

    private fun createNewUser(login: String, password: String) {

    }

    private fun resetPassword(login: String) {

    }
}