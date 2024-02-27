package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel(private val navController: NavController) :
    BaseViewModel<AuthContract.AuthIntent, AuthContract.AuthState>() {
    override fun initialState(): AuthContract.AuthState {
        return AuthContract.AuthState(
            isLoading = false,
            email = "",
            login = "",
            password = "",
            passwordRepeat = ""
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is AuthContract.AuthIntent.NavigateToLogin -> {
                navController.navigate(NavigationGraph.Authentication.LogIn)
            }

            is AuthContract.AuthIntent.NavigateToResetPassword -> {
                navController.navigate(NavigationGraph.Authentication.ResetPassword)
            }

            is AuthContract.AuthIntent.NavigateToSignUp -> {
                navController.navigate(NavigationGraph.Authentication.SignUp)
            }

            is AuthContract.AuthIntent.EditLogin -> setState { copy(login = intent.newLogin) }
            is AuthContract.AuthIntent.EditEmail -> setState { copy(email = intent.newEmail)  }
            is AuthContract.AuthIntent.EditPassword -> setState { copy(password = intent.newPassword) }
            is AuthContract.AuthIntent.EditPasswordRepeat -> setState { copy(passwordRepeat = intent.newPasswordRepeat) }

            is AuthContract.AuthIntent.LogIn -> tryLogIn()
            is AuthContract.AuthIntent.SignUp -> createNewUser()
            is AuthContract.AuthIntent.ResetPassword -> resetPassword()
        }
    }

    private fun tryLogIn() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val successfulLogin = async(Dispatchers.IO) {
                // TODO: replace delay with real check
                delay(500L)
                CurrentUser.tryLogIn(_viewState.value.login, _viewState.value.password)
            }
            if (successfulLogin.await()) {
                navController.navigate(NavigationGraph.MainApp.HomeScreen) {
                    popUpTo(NavigationGraph.Authentication.route) {
                        inclusive = true
                    }
                }
            }
            setState { copy(isLoading = false) }
        }
    }

    private fun createNewUser() {
        // TODO: api call should be here
    }

    private fun resetPassword() {
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