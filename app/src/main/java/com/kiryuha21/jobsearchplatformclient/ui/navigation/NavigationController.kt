package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.kiryuha21.jobsearchplatformclient.ui.screens.HomeScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.LogInScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResetPasswordScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.SignUpScreen
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.HomePageViewModel

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
                    LogInScreen(viewModel = it.sharedAuthViewModel(navController = navController))
                }
                composable(SignUp) {
                    SignUpScreen(viewModel = it.sharedAuthViewModel(navController = navController))
                }
                composable(ResetPassword) {
                    ResetPasswordScreen(viewModel = it.sharedAuthViewModel(navController = navController))
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