package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.kiryuha21.jobsearchplatformclient.ui.screens.HomeScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.LogInScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResetPasswordScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.SignUpScreen

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
                composable(LogIn, enterTransition = enterAnimation()) {
                    LogInScreen(
                        authViewModel = it.sharedViewModel(navController = navController),
                        navController = navController
                    )
                }
                composable(SignUp, enterTransition = enterAnimation()) {
                    SignUpScreen(authViewModel = it.sharedViewModel(navController = navController))
                }
                composable(ResetPassword, enterTransition = enterAnimation()) {
                    ResetPasswordScreen(authViewModel = it.sharedViewModel(navController = navController))
                }
            }
        }
        with(NavigationGraph.MainApp) {
            navigation(
                startDestination = HomeScreen,
                route = route
            ) {
                composable(HomeScreen) {
                    HomeScreen()
                }
            }
        }
    }
}