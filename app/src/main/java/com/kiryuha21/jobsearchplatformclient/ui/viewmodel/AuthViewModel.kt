package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph

class AuthViewModel(private val navController: NavController) :
    BaseViewModel<AuthContract.AuthIntent, AuthContract.AuthState>() {
    var email = mutableStateOf("")
    var name = mutableStateOf("")
    var surname = mutableStateOf("")
    var password = mutableStateOf("")
    override fun initialState(): AuthContract.AuthState {
        return AuthContract.AuthState.PageDefault
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is AuthContract.AuthIntent.NavigateToLogin -> {
                navController.navigate(NavigationGraph.Authentication.LogIn)
                _viewState.value = AuthContract.AuthState.PageDefault
            }
            is AuthContract.AuthIntent.NavigateToResetPassword -> {
                navController.navigate(NavigationGraph.Authentication.ResetPassword)
                _viewState.value = AuthContract.AuthState.PageDefault
            }
            is AuthContract.AuthIntent.NavigateToSignUp -> {
                navController.navigate(NavigationGraph.Authentication.SignUp)
                _viewState.value = AuthContract.AuthState.PageDefault
            }

            is AuthContract.AuthIntent.LogIn -> tryLogIn(intent.login, intent.password)
            is AuthContract.AuthIntent.SignUp -> createNewUser(intent.login, intent.password)
            is AuthContract.AuthIntent.ResetPassword -> resetPassword(intent.login)
        }
    }

    private fun tryLogIn(login: String, password: String) {

    }

    private fun createNewUser(login: String, password: String) {

    }

    private fun resetPassword(login: String) {

    }
}