package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.kiryuha21.jobsearchplatformclient.data.local.datastore.TokenDataStore
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.NavigationViewModel

@Composable
fun NavigationController() {
    val navController = rememberNavController()
    val shouldShowTopBar = rememberSaveable { mutableStateOf(false) }

    val ctx = LocalContext.current
    val navigationVM: NavigationViewModel = viewModel(factory = NavigationViewModel.provideFactory(
        tokenDatasourceProvider = TokenDataStore(ctx),
        logoutCallback = {
            navController.navigate(NavigationGraph.Authentication.LOG_IN) {
                popUpTo(NavigationGraph.MainApp.NAV_ROUTE) {
                    inclusive = true
                }
            }
        })
    )

    MainAppScaffold(
        navigateFunction = {
            navigationVM.navigateTo(it)
            navController.navigate(it)
        },
        onLogOut = { navigationVM.logOut() },
        onSetImage = { navigationVM.setUserImage(it) },
        shouldShowTopBar = shouldShowTopBar.value,
        selectedNavigationIndex = navigationVM.currentIndex
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationGraph.Authentication.NAV_ROUTE,
            modifier = Modifier.padding(paddingValues)
        ) {
            navigation(
                startDestination = NavigationGraph.Authentication.LOG_IN,
                route = NavigationGraph.Authentication.NAV_ROUTE
            ) {
                addAuthentication(navController, shouldShowTopBar)
            }

            navigation(
                startDestination = NavigationGraph.MainApp.HOME_SCREEN,
                route = NavigationGraph.MainApp.NAV_ROUTE
            ) {
                addCommonDestinations(
                    navController = navController,
                    shouldShowAppBar = shouldShowTopBar,
                    onNavigateBack = { navigationVM.navigateBack() },
                    onNavigationForward = { navigationVM.navigateTo(it) }
                )
                addResumeDestinations(
                    navController = navController,
                    shouldShowAppBar = shouldShowTopBar,
                    onNavigateBack = { navigationVM.navigateBack() },
                    onNavigationForward = { navigationVM.navigateTo(it) }
                )
                addVacancyDestinations(
                    navController = navController,
                    shouldShowAppBar = shouldShowTopBar,
                    onNavigateBack = { navigationVM.navigateBack() },
                    onNavigationForward = { navigationVM.navigateTo(it) }
                )
            }
        }
    }
}