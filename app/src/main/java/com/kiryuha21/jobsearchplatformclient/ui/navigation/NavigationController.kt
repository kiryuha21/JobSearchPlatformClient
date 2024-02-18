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
                            viewModel.processIntent(AuthContract.AuthIntent.LogIn {
                                with(viewModel.userData) {
                                    userVM.processIntent(
                                        UserContract.UserIntent.TryLogIn(login.value, password.value)
                                    )
                                }
                            })
                        }
                    )
                }
                composable(SignUp) {
                    val viewModel = it.sharedAuthViewModel(navController = navController) as AuthViewModel
                    SignUpScreen(
                        viewState = viewModel.viewState,
                        userData = viewModel.userData
                    ) {
                        viewModel.processIntent(
                            AuthContract.AuthIntent.SignUp {
                                userVM.processIntent(
                                    with (viewModel.userData) {
                                        UserContract.UserIntent.TrySignUp(login.value, email.value, password.value)
                                    }
                                )
                            }
                        )
                    }
                }
                composable(ResetPassword) {
                    val viewModel = it.sharedAuthViewModel(navController = navController) as AuthViewModel
                    ResetPasswordScreen(
                        viewState = viewModel.viewState,
                        userData = viewModel.userData
                    ) {
                        viewModel.processIntent(
                            AuthContract.AuthIntent.ResetPassword {
                                userVM.processIntent(
                                    UserContract.UserIntent.TryResetPassword(viewModel.userData.email.value)
                                )
                            }
                        )
                    }
                }
            }
        }
        with(NavigationGraph.MainApp) {
            navigation(
                startDestination = HomeScreen,
                route = route
            ) {
                composable(HomeScreen) {
                    val viewModel: HomePageViewModel = viewModel(factory = HomePageViewModel.provideFactory(navController))
                    HomeScreen(viewModel)
                }
            }
        }
    }
}