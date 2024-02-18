package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.UserContract
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserDataStates {
    var email = mutableStateOf("")
    var login = mutableStateOf("")
    var password = mutableStateOf("")
    var passwordRepeat = mutableStateOf("")
}

class AuthViewModel(private val navController: NavController) :
    BaseViewModel<AuthContract.AuthIntent, AuthContract.AuthState>() {
    var userData = UserDataStates()
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

            is AuthContract.AuthIntent.LogIn -> tryLogIn(intent.userDelegate)
            is AuthContract.AuthIntent.SignUp -> createNewUser(intent.userDelegate)
            is AuthContract.AuthIntent.ResetPassword -> resetPassword(intent.userDelegate)
        }
    }

    private fun tryLogIn(userDelegate: (UserContract.UserIntent.TryLogIn) -> Unit) {
        _viewState.value = AuthContract.AuthState.Loading
        viewModelScope.launch {
            val successfulLogin = async(Dispatchers.IO) {
                // TODO: replace delay with real check
                delay(500L)
                true
            }
            if (successfulLogin.await()) {
                navController.navigate(NavigationGraph.MainApp.HomeScreen) {
                    popUpTo(NavigationGraph.Authentication.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    private fun createNewUser(userDelegate: (UserContract.UserIntent.TrySignUp) -> Unit) {
        // TODO: api call should be here
    }

    private fun resetPassword(userDelegate: (UserContract.UserIntent.TryResetPassword) -> Unit) {
        // TODO: api call should be here
    }

    companion object {
        fun provideFactory(navController: NavController) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return AuthViewModel(navController) as T
                }
            }
    }
}