package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.local.datastore.TokenDataStore
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.UserDTO
import com.kiryuha21.jobsearchplatformclient.di.AuthToken
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import com.kiryuha21.jobsearchplatformclient.util.networkCallWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val navController: NavController,
    private val tokenDatasourceProvider: TokenDataStore
) : BaseViewModel<AuthContract.AuthIntent, AuthContract.AuthState>() {

    override fun initialState(): AuthContract.AuthState {
        return AuthContract.AuthState(
            isLoading = true,
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
            is AuthContract.AuthIntent.CheckRefreshToken -> checkRefreshToken()
        }
    }

    private fun navigateToMainApp() {
        navController.navigate(NavigationGraph.MainApp.NAV_ROUTE) {
            popUpTo(NavigationGraph.Authentication.NAV_ROUTE) {
                inclusive = true
            }
        }
    }

    private fun checkRefreshToken() {
        viewModelScope.launch {
            val token = tokenDatasourceProvider.getRefreshToken(this).value
            val username = if (token != null) {
                withContext(Dispatchers.IO) {
                    networkCallWithReturnWrapper(
                        networkCall = { AuthToken.getLoginFromStoredToken(token) }
                    )
                }
            } else null

            if (username != null && networkCallWrapper({ CurrentUser.loadUserInfo(username) })) {
                navigateToMainApp()
            } else {
                setState { copy(isLoading = false) }
            }
        }
    }

    private fun tryLogIn() {
        setState { copy(isLoading = true) }

        viewModelScope.launch {
            val successfulLogin = withContext(Dispatchers.IO) {
                CurrentUser.tryLogIn(
                    viewState.username,
                    viewState.password
                )
            }

            if (successfulLogin) {
                withContext(Dispatchers.IO) {
                    tokenDatasourceProvider.updateRefreshToken(AuthToken.getRefreshToken() ?: throw Exception("this shouldn't happen"))
                }
                navigateToMainApp()
            } else {
                setState { copy(isLoading = false, isError = true) }
            }
        }
    }

    private fun createNewUser() {
        setState { copy(isLoading = true) }
        viewModelScope.launch {
            val successfulRegister = withContext(Dispatchers.IO) {
                CurrentUser.trySignUp(
                    UserDTO.SignUpUserDTO(
                        email = viewState.email,
                        username = viewState.username,
                        password = viewState.password,
                        role = viewState.role
                    )
                )
            }

            if (successfulRegister) {
                withContext(Dispatchers.IO) {
                    tokenDatasourceProvider.updateRefreshToken(AuthToken.getRefreshToken() ?: throw Exception("this shouldn't happen"))
                }
                navigateToMainApp()
            } else {
                setState { copy(isLoading = false, isError = true) }
            }
        }
    }

    private fun resetPassword() {
        // TODO: api call should be here
    }

    companion object {
        fun provideFactory(navController: NavController, tokenDatasourceProvider: TokenDataStore) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return AuthViewModel(navController, tokenDatasourceProvider) as T
                }
            }
    }
}