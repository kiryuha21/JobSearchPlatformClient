package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.kiryuha21.jobsearchplatformclient.data.local.datastore.TokenDataStore
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.EmployerHomeViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ResumeDetailsViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.VacancyDetailsViewModel

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedAuthViewModel(
    navController: NavController,
    tokenDatasourceProvider: TokenDataStore
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel(
        factory = AuthViewModel.provideFactory(navController, tokenDatasourceProvider)
    )
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry, factory = AuthViewModel.provideFactory(navController, tokenDatasourceProvider))
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedResumeDetailsViewModel(
    navController: NavController,
    noinline navigateToProfile: () -> Unit,
    noinline navigateToProfileWithPop: () -> Unit,
    noinline navigateToEdit: () -> Unit
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel(
        factory = ResumeDetailsViewModel.provideFactory(
            navigateToProfile = navigateToProfile,
            navigateToProfileWithPop = navigateToProfileWithPop,
            navigateToEdit = navigateToEdit
        )
    )
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry, factory = ResumeDetailsViewModel.provideFactory(
        navigateToProfile = navigateToProfile,
        navigateToProfileWithPop = navigateToProfileWithPop,
        navigateToEdit = navigateToEdit
    ))
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedVacancyDetailsViewModel(
    navController: NavController,
    noinline navigateToProfile: () -> Unit,
    noinline navigateToProfileWithPop: () -> Unit,
    noinline navigateToEdit: () -> Unit
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel(
        factory = VacancyDetailsViewModel.provideFactory(
            navigateToProfile = navigateToProfile,
            navigateToProfileWithPop = navigateToProfileWithPop,
            navigateToEdit = navigateToEdit
        )
    )
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry, factory = VacancyDetailsViewModel.provideFactory(
        navigateToProfile = navigateToProfile,
        navigateToProfileWithPop = navigateToProfileWithPop,
        navigateToEdit = navigateToEdit
    ))
}