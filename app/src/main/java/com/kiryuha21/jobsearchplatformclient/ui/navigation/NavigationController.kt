package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.screens.HomeScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.LogInScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ProfileScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResetPasswordScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResumeDetailsScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.SettingsScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.SignUpScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.VacancyDetailsScreen
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.HomePageViewModel

fun NavGraphBuilder.addAuthentication(
    navController: NavController,
    shouldShowAppBar: MutableState<Boolean>
) = with(NavigationGraph.Authentication) {
    navigation(
        startDestination = LOG_IN,
        route = NAV_ROUTE
    ) {
        composable(LOG_IN) {
            val viewModel =
                it.sharedAuthViewModel(navController = navController) as AuthViewModel
            LaunchedEffect(key1 = Unit) {
                shouldShowAppBar.value = false
            }

            LogInScreen(
                state = viewModel.viewState.value,
                onUsernameFieldEdited = { newLogin ->
                    viewModel.processIntent(AuthContract.AuthIntent.EditLogin(newLogin))
                },
                onPasswordFieldEdited = { newPassword ->
                    viewModel.processIntent(AuthContract.AuthIntent.EditPassword(newPassword))
                },
                onResetPassword = { viewModel.processIntent(AuthContract.AuthIntent.NavigateToResetPassword) },
                onSignUp = { viewModel.processIntent(AuthContract.AuthIntent.NavigateToSignUp) },
                onLogin = {
                    viewModel.processIntent(AuthContract.AuthIntent.LogIn)
                }
            )
        }
        composable(SIGN_UP) {
            val viewModel =
                it.sharedAuthViewModel(navController = navController) as AuthViewModel

            SignUpScreen(
                state = viewModel.viewState.value,
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
                    viewModel.processIntent(
                        AuthContract.AuthIntent.EditPasswordRepeat(
                            newPasswordRepeat
                        )
                    )
                },
                onRoleToggled = { toggleElement ->
                    viewModel.processIntent(
                        AuthContract.AuthIntent.EditRole(toggleElement.role)
                    )
                },
                onErrorFix = { viewModel.processIntent(AuthContract.AuthIntent.FixError) },
                onRegister = {
                    viewModel.processIntent(
                        AuthContract.AuthIntent.SignUp
                    )
                })
        }
        composable(RESET_PASSWORD) {
            val viewModel =
                it.sharedAuthViewModel(navController = navController) as AuthViewModel

            ResetPasswordScreen(
                state = viewModel.viewState.value,
                onEmailFieldEdited = { newEmail ->
                    viewModel.processIntent(AuthContract.AuthIntent.EditEmail(newEmail))
                },
                onReset = {
                    viewModel.processIntent(
                        AuthContract.AuthIntent.ResetPassword
                    )
                }
            )
        }
    }
}

fun NavGraphBuilder.addMainApp(
    navController: NavController,
    shouldShowAppBar: MutableState<Boolean>
) = with(NavigationGraph.MainApp) {
    navigation(
        startDestination = HOME_SCREEN,
        route = NAV_ROUTE
    ) {
        composable(HOME_SCREEN) {
            LaunchedEffect(Unit) {
                shouldShowAppBar.value = true
            }

            val viewModel: HomePageViewModel = it.sharedHomePageViewModel(navController = navController)
            HomeScreen(viewModel)
        }
        composable(PROFILE) {
            LaunchedEffect(Unit) {
                shouldShowAppBar.value = true
            }

            val viewModel: HomePageViewModel = it.sharedHomePageViewModel(navController = navController)
            ProfileScreen(viewModel)
        }
        composable(SETTINGS) {
            LaunchedEffect(Unit) {
                shouldShowAppBar.value = true
            }

            SettingsScreen({}, {}, {})
        }
        composable(VACANCY_DETAILS) {
            LaunchedEffect(Unit) {
                shouldShowAppBar.value = false
            }

            VacancyDetailsScreen(
                editable = CurrentUser.userInfo.value.role != UserRole.Worker,
                vacancyId = it.arguments?.getString("vacancyId"),
                viewModel = it.sharedHomePageViewModel(navController = navController)
            )
        }
        composable(RESUME_DETAILS) {
            LaunchedEffect(Unit) {
                shouldShowAppBar.value = false
            }

            ResumeDetailsScreen(
                editable = CurrentUser.userInfo.value.role == UserRole.Worker,
                resumeId = it.arguments?.getString("resumeId"),
                viewModel = it.sharedHomePageViewModel(navController = navController)
            )
        }
    }
}

@Composable
fun NavigationController() {
    val navController = rememberNavController()
    val shouldShowTopBar = rememberSaveable { mutableStateOf(true) }

    MainAppScaffold(
        navigateFunction = navController::navigate,
        onLogOut = {
            navController.navigate(NavigationGraph.Authentication.LOG_IN) {
                popUpTo(NavigationGraph.MainApp.NAV_ROUTE) {
                    inclusive = true
                }
            }
        },
        shouldShowTopBar = shouldShowTopBar.value
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationGraph.Authentication.NAV_ROUTE,
            modifier = Modifier.padding(paddingValues)
        ) {
            addAuthentication(navController, shouldShowTopBar)
            addMainApp(navController, shouldShowTopBar)
        }
    }

}