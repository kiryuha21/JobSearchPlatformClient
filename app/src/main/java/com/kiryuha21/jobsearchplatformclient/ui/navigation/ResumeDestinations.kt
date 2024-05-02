package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.ui.components.special.OnBackPressedWithSuper
import com.kiryuha21.jobsearchplatformclient.ui.contract.ResumeDetailsContract
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResumeDetailsScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResumeEditScreen
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ResumeDetailsViewModel

@Composable
fun AnimatedContentScope.ResumeCommonEditScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    shouldShowAppBar: MutableState<Boolean>,
    onNavigateBack: () -> Unit,
    onNavigationForward: (String) -> Unit,
    isNewResume: Boolean
) {
    LaunchedEffect(Unit) {
        shouldShowAppBar.value = false
    }
    OnBackPressedWithSuper(onNavigateBack)

    val viewModel: ResumeDetailsViewModel = backStackEntry.sharedResumeDetailsViewModel(
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
            navController.navigate(NavigationGraph.MainApp.RESUME_EDIT)
            onNavigationForward(NavigationGraph.MainApp.RESUME_EDIT)
        }
    )

    val resume = if (isNewResume) {
        Resume()
    } else {
        viewModel.viewState.openedResume ?: throw Exception("resume can't be null here")
    }

    ResumeEditScreen(
        initResume = resume,
        isLoading = viewModel.viewState.isSavingResume,
        loadingText = viewModel.viewState.loadingText,
        onUpdateResume = { updatedResume, bitmap ->
            if (isNewResume) {
                viewModel.processIntent(ResumeDetailsContract.Intent.CreateResume(updatedResume, bitmap))
            } else {
                viewModel.processIntent(ResumeDetailsContract.Intent.EditResume(updatedResume, bitmap))
            }
            onNavigationForward(NavigationGraph.MainApp.PROFILE)
        }
    )
}

fun NavGraphBuilder.addResumeDestinations(
    navController: NavController,
    shouldShowAppBar: MutableState<Boolean>,
    onNavigateBack: () -> Unit,
    onNavigationForward: (String) -> Unit
) = with(NavigationGraph.MainApp) {
    composable(RESUME_DETAILS) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = false
        }
        OnBackPressedWithSuper(onNavigateBack)

        val viewModel: ResumeDetailsViewModel = backStack.sharedResumeDetailsViewModel(
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
                navController.navigate(RESUME_EDIT)
                onNavigationForward(RESUME_EDIT)
            }
        )

        val resumeId = backStack.arguments?.getString("resumeId") ?: throw Exception("resumeId should be passed via backstack!")
        LaunchedEffect(key1 = Unit) {
            viewModel.processIntent(ResumeDetailsContract.Intent.LoadResume(resumeId))
        }

        ResumeDetailsScreen(
            editable = CurrentUser.info.role == UserRole.Worker,
            onEdit = { viewModel.processIntent(ResumeDetailsContract.Intent.OpenEdit) },
            onDelete = { viewModel.processIntent(ResumeDetailsContract.Intent.DeleteResume) },
            state = viewModel.viewState
        )
    }
    composable(RESUME_CREATION) { backStack ->
        ResumeCommonEditScreen(
            navController = navController,
            backStackEntry = backStack,
            shouldShowAppBar = shouldShowAppBar,
            onNavigateBack = onNavigateBack,
            onNavigationForward = onNavigationForward,
            isNewResume = true
        )
    }
    composable(RESUME_EDIT) { backStack ->
        ResumeCommonEditScreen(
            navController = navController,
            backStackEntry = backStack,
            shouldShowAppBar = shouldShowAppBar,
            onNavigateBack = onNavigateBack,
            onNavigationForward = onNavigationForward,
            isNewResume = false
        )
    }
}