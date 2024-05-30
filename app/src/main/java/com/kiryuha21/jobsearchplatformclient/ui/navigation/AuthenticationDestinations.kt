package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kiryuha21.jobsearchplatformclient.data.local.datastore.AppDataStore
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.screens.LogInScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResetPasswordScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.SignUpScreen
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel

fun NavGraphBuilder.addAuthentication(
    navController: NavController,
    shouldShowAppBar: MutableState<Boolean>
) = with(NavigationGraph.Authentication) {
    composable(LOG_IN) {
        LaunchedEffect(key1 = Unit) {
            shouldShowAppBar.value = false
        }

        val viewModel: AuthViewModel = it.sharedAuthViewModel(
            navController,
            AppDataStore(LocalContext.current)
        )

        LaunchedEffect(key1 = Unit) {
            viewModel.processIntent(AuthContract.AuthIntent.CheckRefreshToken)
        }

        LogInScreen(
            state = viewModel.viewState,
            onUsernameFieldEdited = { newLogin ->
                viewModel.processIntent(AuthContract.AuthIntent.EditLogin(newLogin))
            },
            onPasswordFieldEdited = { newPassword ->
                viewModel.processIntent(AuthContract.AuthIntent.EditPassword(newPassword))
            },
            onResetPassword = { viewModel.processIntent(AuthContract.AuthIntent.NavigateToResetPassword) },
            onSignUp = { viewModel.processIntent(AuthContract.AuthIntent.NavigateToSignUp) },
            onLogin = { viewModel.processIntent(AuthContract.AuthIntent.LogIn) },
            onErrorFix = { viewModel.processIntent(AuthContract.AuthIntent.FixError) }
        )
    }
    composable(SIGN_UP) {
        val viewModel: AuthViewModel = it.sharedAuthViewModel(
            navController,
            AppDataStore(LocalContext.current)
        )

        SignUpScreen(
            state = viewModel.viewState,
            onLoginFieldUpdated = { newLogin ->
                viewModel.processIntent(AuthContract.AuthIntent.EditLogin(newLogin))
            },
            onEmailFieldUpdated = { newEmail ->
                viewModel.processIntent(AuthContract.AuthIntent.EditEmail(newEmail))
            },
            onPasswordFieldUpdated = { newPassword ->
                viewModel.processIntent(AuthContract.AuthIntent.EditPassword(newPassword))
            },
            onPasswordRepeatFieldUpdated = { newPasswordRepeat ->
                viewModel.processIntent(AuthContract.AuthIntent.EditPasswordRepeat(newPasswordRepeat))
            },
            onRoleToggled = { toggleElement ->
                viewModel.processIntent(AuthContract.AuthIntent.EditRole(toggleElement.role))
            },
            onErrorFix = { viewModel.processIntent(AuthContract.AuthIntent.FixError) },
            onRegister = { viewModel.processIntent(AuthContract.AuthIntent.SignUp) })
    }
    composable(RESET_PASSWORD) {
        val viewModel: AuthViewModel = it.sharedAuthViewModel(
            navController,
            AppDataStore(LocalContext.current)
        )

        ResetPasswordScreen(
            state = viewModel.viewState,
            onEmailFieldEdited = { newEmail ->
                viewModel.processIntent(AuthContract.AuthIntent.EditEmail(newEmail))
            },
            onReset = { viewModel.processIntent(AuthContract.AuthIntent.ResetPassword) }
        )
    }
}