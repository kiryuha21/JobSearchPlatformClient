package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.UserContract
import com.kiryuha21.jobsearchplatformclient.ui.screens.HomeScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.LogInScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResetPasswordScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.SignUpScreen
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.HomePageViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.UserViewModel

// TODO: split into build-functions
@Composable
fun NavigationController() {
    val userVM: UserViewModel = viewModel()
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
                            viewModel.processIntent(AuthContract.AuthIntent.LogIn(userVM::processIntent))
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
                                AuthContract.AuthIntent.SignUp(userVM::processIntent)
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
                                AuthContract.AuthIntent.ResetPassword(userVM::processIntent)
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
                        (userVM.viewState as UserContract.UserState.Authorized).user.login
                    )
                }
                composable(Profile) {

                }
            }
        }
    }
}