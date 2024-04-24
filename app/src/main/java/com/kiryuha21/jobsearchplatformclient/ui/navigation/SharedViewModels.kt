package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.kiryuha21.jobsearchplatformclient.data.local.datastore.TokenDataStore
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.MainAppViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ResumeDetailsViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.WorkerHomeViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.WorkerProfileViewModel

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
inline fun <reified T : ViewModel> NavBackStackEntry.sharedMainAppViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel(
        factory = MainAppViewModel.provideFactory(navController)
    )
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry, factory = MainAppViewModel.provideFactory(navController))
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedWorkerHomeViewModel(
    navController: NavController,
    noinline openVacancyCallback: (String) -> Unit
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel(
        factory = WorkerHomeViewModel.provideFactory(openVacancyCallback)
    )
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry, factory = WorkerHomeViewModel.provideFactory(openVacancyCallback))
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedResumeDetailsViewModel(
    navController: NavController,
    noinline navigateToEdit: () -> Unit,
    noinline navigateCallback: () -> Unit,
    noinline navigateWithPopCallback: () -> Unit
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel(
        factory = ResumeDetailsViewModel.provideFactory(
            navigateToEdit = navigateToEdit,
            navigateCallback = navigateCallback,
            navigateWithPopCallback = navigateWithPopCallback
        )
    )
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry, factory = ResumeDetailsViewModel.provideFactory(
        navigateToEdit = navigateToEdit,
        navigateCallback = navigateCallback,
        navigateWithPopCallback = navigateWithPopCallback
    ))
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedWorkerProfileViewModel(
    navController: NavController,
    noinline openResumeDetailsCallback: (String) -> Unit,
    noinline createResumeCallback: () -> Unit
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel(
        factory = WorkerProfileViewModel.provideFactory(
            openResumeDetailsCallback = openResumeDetailsCallback,
            createResumeCallback = createResumeCallback
        )
    )
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry, factory = WorkerProfileViewModel.provideFactory(
        openResumeDetailsCallback = openResumeDetailsCallback,
        createResumeCallback = createResumeCallback
    ))
}