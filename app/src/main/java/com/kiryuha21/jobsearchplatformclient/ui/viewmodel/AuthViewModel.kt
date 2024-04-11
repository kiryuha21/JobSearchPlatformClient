package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.UserDTO
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph
import kotlinx.coroutines.launch

class AuthViewModel(private val navController: NavController) :
    BaseViewModel<AuthContract.AuthIntent, AuthContract.AuthState>() {

    override fun initialState(): AuthContract.AuthState {
        return AuthContract.AuthState(
            isLoading = false,
            isError = false,
            email = "",
            username = "",
            role = UserRole.Worker,
            password = "",
            passwordRepeat = ""
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is AuthContract.AuthIntent.NavigateToLogin -> {
                navController.navigate(NavigationGraph.Authentication.LOG_IN)
                setState { copy(isError = false) }
            }

            is AuthContract.AuthIntent.NavigateToResetPassword -> {
                navController.navigate(NavigationGraph.Authentication.RESET_PASSWORD)
                setState { copy(isError = false) }
            }

            is AuthContract.AuthIntent.NavigateToSignUp -> {
                navController.navigate(NavigationGraph.Authentication.SIGN_UP)
                setState { copy(isError = false) }
            }

            is AuthContract.AuthIntent.FixError -> setState { copy(isError = false) }

            is AuthContract.AuthIntent.EditLogin -> setState { copy(username = intent.newLogin) }
            is AuthContract.AuthIntent.EditEmail -> setState { copy(email = intent.newEmail) }
            is AuthContract.AuthIntent.EditPassword -> setState { copy(password = intent.newPassword) }
            is AuthContract.AuthIntent.EditPasswordRepeat -> setState { copy(passwordRepeat = intent.newPasswordRepeat) }
            is AuthContract.AuthIntent.EditRole -> setState { copy(role = intent.newRole) }

            is AuthContract.AuthIntent.LogIn -> tryLogIn()
            is AuthContract.AuthIntent.SignUp -> createNewUser()
            is AuthContract.AuthIntent.ResetPassword -> resetPassword()
        }
    }

    private fun tryLogIn() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val successfulLogin = CurrentUser.tryLogIn(viewState.username, viewState.password)
            if (successfulLogin) {
                setState { copy(isLoading = false) }
                navController.navigate(NavigationGraph.MainApp.HOME_SCREEN) {
                    popUpTo(NavigationGraph.Authentication.NAV_ROUTE) {
                        inclusive = true
                    }
                }
            } else {
                setState { copy(isLoading = false, isError = true) }
            }
        }
    }

    private fun createNewUser() {
        setState { copy(isLoading = true) }
        viewModelScope.launch {
            val successfulRegister = CurrentUser.trySignUp(
                UserDTO.SignUpUserDTO(
                    email = viewState.email,
                    username = viewState.username,
                    password = viewState.password,
                    role = viewState.role
                )
            )
            if (successfulRegister) {
                setState { copy(isLoading = false) }
                navController.navigate(NavigationGraph.MainApp.HOME_SCREEN) {
                    popUpTo(NavigationGraph.Authentication.NAV_ROUTE) {
                        inclusive = true
                    }
                }
            } else {
                setState { copy(isLoading = false, isError = true) }
            }
        }
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