package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.components.special.OnBackPressedWithSuper
import com.kiryuha21.jobsearchplatformclient.ui.contract.VacancyDetailsContract
import com.kiryuha21.jobsearchplatformclient.ui.screens.VacancyDetailsScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.VacancyEditScreen
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.VacancyDetailsViewModel

@Composable
fun AnimatedContentScope.VacancyCommonEditScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    shouldShowAppBar: MutableState<Boolean>,
    onNavigateBack: () -> Unit,
    onNavigationForward: (String) -> Unit,
    isNewVacancy: Boolean
) {
    LaunchedEffect(Unit) {
        shouldShowAppBar.value = false
    }
    OnBackPressedWithSuper(onNavigateBack)

    val viewModel: VacancyDetailsViewModel = backStackEntry.sharedVacancyDetailsViewModel(
        navController = navController,
        navigateToProfile = {
            navController.navigate(NavigationGraph.MainApp.PROFILE)
            onNavigationForward(NavigationGraph.MainApp.PROFILE)
        },
        navigateToProfileWithPop = {
            navController.popBackStack()
            onNavigateBack()
            navController.navigate(NavigationGraph.MainApp.PROFILE)
            onNavigationForward(NavigationGraph.MainApp.PROFILE)
        },
        navigateToEdit = {
            navController.navigate(NavigationGraph.MainApp.VACANCY_EDIT)
            onNavigationForward(NavigationGraph.MainApp.VACANCY_EDIT)
        }
    )

    val vacancy = if (isNewVacancy) {
        Vacancy()
    } else {
        viewModel.viewState.openedVacancy ?: throw Exception("vacancy can't be null here")
    }

    VacancyEditScreen(
        initVacancy = vacancy,
        isLoading = viewModel.viewState.isSavingVacancy,
        loadingText = viewModel.viewState.loadingText,
        onUpdateVacancy = { updatedVacancy, bitmap ->
            if (isNewVacancy) {
                viewModel.processIntent(VacancyDetailsContract.Intent.CreateVacancy(updatedVacancy, bitmap))
            } else {
                viewModel.processIntent(VacancyDetailsContract.Intent.EditVacancy(updatedVacancy, bitmap))
            }
            onNavigationForward(NavigationGraph.MainApp.PROFILE)
        }
    )
}

fun NavGraphBuilder.addVacancyDestinations(
    navController: NavController,
    shouldShowAppBar: MutableState<Boolean>,
    onNavigateBack: () -> Unit,
    onNavigationForward: (String) -> Unit
) = with(NavigationGraph.MainApp) {
    composable(VACANCY_DETAILS) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = false
        }
        OnBackPressedWithSuper(onNavigateBack)

        val viewModel: VacancyDetailsViewModel = backStack.sharedVacancyDetailsViewModel(
            navController = navController,
            navigateToProfile = {
                navController.navigate(PROFILE)
                onNavigationForward(PROFILE)
            },
            navigateToProfileWithPop = {
                navController.popBackStack()
                onNavigateBack()
                navController.navigate(PROFILE)
                onNavigationForward(PROFILE)
            },
            navigateToEdit = {
                navController.navigate(VACANCY_EDIT)
                onNavigationForward(VACANCY_EDIT)
            }
        )

        val vacancyId = backStack.arguments?.getString("vacancyId") ?: throw Exception("vacancyId should be passed via backstack!")
        LaunchedEffect(key1 = Unit) {
            viewModel.processIntent(VacancyDetailsContract.Intent.LoadVacancy(vacancyId))
        }

        VacancyDetailsScreen(
            onEdit = { viewModel.processIntent(VacancyDetailsContract.Intent.OpenEdit) },
            onDelete = { viewModel.processIntent(VacancyDetailsContract.Intent.DeleteVacancy) },
            onCreateResponse = {
                navController.navigate("$RESPONSE_CREATION_BASE/$vacancyId")
                onNavigationForward("$RESPONSE_CREATION_BASE/$vacancyId")
            },
            state = viewModel.viewState
        )
    }
    composable(VACANCY_EDIT) { backStack ->
        VacancyCommonEditScreen(
            navController = navController,
            backStackEntry = backStack,
            shouldShowAppBar = shouldShowAppBar,
            onNavigateBack = onNavigateBack,
            onNavigationForward = onNavigationForward,
            isNewVacancy = false
        )
    }
    composable(VACANCY_CREATION) {backStack ->
        VacancyCommonEditScreen(
            navController = navController,
            backStackEntry = backStack,
            shouldShowAppBar = shouldShowAppBar,
            onNavigateBack = onNavigateBack,
            onNavigationForward = onNavigationForward,
            isNewVacancy = true
        )
    }
}