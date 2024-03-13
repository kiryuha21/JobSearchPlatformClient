package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.HomePageViewModel

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedAuthViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel(
        factory = AuthViewModel.provideFactory(navController)
    )
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry, factory = AuthViewModel.provideFactory(navController))
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedHomePageViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel(
        factory = HomePageViewModel.provideFactory(navController)
    )
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry, factory = HomePageViewModel.provideFactory(navController))
}