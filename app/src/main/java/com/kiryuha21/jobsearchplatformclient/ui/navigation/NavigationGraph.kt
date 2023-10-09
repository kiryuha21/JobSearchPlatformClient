package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

@Composable
inline fun<reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}

object NavigationGraph {
    object Authentication {
        const val route = "Authentication"
        const val LogIn = "Log In"
        const val SignUp = "Sign Up"
        const val ResetPassword = "Reset Password"
    }
    object MainApp {
        const val route = "MainApp"
        const val HomeScreen = "Home Screen"
    }
}