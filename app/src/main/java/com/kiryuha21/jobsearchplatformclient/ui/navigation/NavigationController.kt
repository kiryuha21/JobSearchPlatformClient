package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.kiryuha21.jobsearchplatformclient.data.model.User
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.screens.HomeScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.LogInScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResetPasswordScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.SignUpScreen
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.HomePageViewModel

// TODO: split into build-functions
@Composable
fun NavigationController() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavigationGraph.Authentication.route,
    ) {
        with(NavigationGraph.Authentication) {
            navigation(
                startDestination = LogIn,
                route = route
            ) {
                composable(LogIn) {
                    val viewModel = it.sharedAuthViewModel(navController = navController) as AuthViewModel
                    LogInScreen(
                        viewState = viewModel.viewState,
                        userData = viewModel.userData,
                        onResetPassword = { viewModel.processIntent(AuthContract.AuthIntent.NavigateToResetPassword) },
                        onSignUp = { viewModel.processIntent(AuthContract.AuthIntent.NavigateToSignUp) },
                        onLogin = {
                            viewModel.processIntent(AuthContract.AuthIntent.LogIn)
                        }
                    )
                }
                composable(SignUp) {
                    val viewModel =
                        it.sharedAuthViewModel(navController = navController) as AuthViewModel
                    SignUpScreen(
                        viewState = viewModel.viewState,
                        userData = viewModel.userData,
                        onRegister = {
                            viewModel.processIntent(
                                AuthContract.AuthIntent.SignUp
                            )
                        })
                }
                composable(ResetPassword) {
                    val viewModel = it.sharedAuthViewModel(navController = navController) as AuthViewModel
                    ResetPasswordScreen(
                        viewState = viewModel.viewState,
                        userData = viewModel.userData,
                        onReset = {
                            viewModel.processIntent(
                                AuthContract.AuthIntent.ResetPassword
                            )
                        }
                    )
                }
            }
        }
        with(NavigationGraph.MainApp) {
            navigation(
                startDestination = HomeScreen,
                route = route
            ) {
                composable(HomeScreen) {
                    val viewModel: HomePageViewModel =
                        viewModel(factory = HomePageViewModel.provideFactory(navController))
                    HomeScreen(
                        viewModel,
                        navController::navigate,
                        User.userInfo?.login ?: "Unknown user"
                    )
                }
                composable(Profile) {

                }
            }
        }
    }
}