package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.special.OnBackPressedWithSuper
import com.kiryuha21.jobsearchplatformclient.ui.contract.EmployerHomeContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.EmployerProfileContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.SettingsContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerHomeContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerProfileContract
import com.kiryuha21.jobsearchplatformclient.ui.screens.EmployerHomeScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.EmployerProfileScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.SettingsScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.WorkerHomeScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.WorkerProfileScreen
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.EmployerHomeViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.EmployerProfileViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.SettingsViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.WorkerHomeViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.WorkerProfileViewModel

fun NavGraphBuilder.addCommonDestinations(
    navController: NavController,
    shouldShowAppBar: MutableState<Boolean>,
    onNavigateBack: () -> Unit,
    onNavigationForward: (String) -> Unit
) = with(NavigationGraph.MainApp) {
    composable(HOME_SCREEN) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = true
        }
        OnBackPressedWithSuper(onNavigateBack)

        when (CurrentUser.info.role) {
            UserRole.Worker -> {
                val vm : WorkerHomeViewModel = backStack.sharedWorkerHomeViewModel(
                    navController = navController,
                    openVacancyCallback = { vacancyId ->
                        navController.navigate("$VACANCY_DETAILS_BASE/$vacancyId")
                        onNavigationForward("$VACANCY_DETAILS_BASE/$vacancyId")
                    }
                )

                WorkerHomeScreen(
                    state = vm.viewState,
                    loadVacancies = {
                        vm.processIntent(WorkerHomeContract.Intent.LoadVacancies(it))
                    },
                    openVacancyDetails = { vacancyId ->
                        vm.processIntent(WorkerHomeContract.Intent.OpenVacancyDetails(vacancyId))
                    }
                )
            }

            UserRole.Employer -> {
                val vm : EmployerHomeViewModel = backStack.sharedEmployerHomeViewModel(
                    navController = navController,
                    openResumeCallback = { resumeId ->
                        navController.navigate("$RESUME_DETAILS_BASE/$resumeId")
                        onNavigationForward("$RESUME_DETAILS_BASE/$resumeId")
                    }
                )

                EmployerHomeScreen(
                    state = vm.viewState,
                    loadResumes = {
                        vm.processIntent(EmployerHomeContract.Intent.LoadResumes(it))
                    },
                    openResumeDetails = { resumeId ->
                        vm.processIntent(EmployerHomeContract.Intent.OpenResumeDetails(resumeId))
                    }
                )
            }

            UserRole.Undefined -> LoadingComponent(
                modifier = Modifier.fillMaxSize(),
                description = "Logging out"
            )
        }
    }
    composable(PROFILE) {
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = true
        }
        OnBackPressedWithSuper(onNavigateBack)

        when (CurrentUser.info.role) {
            UserRole.Worker -> {
                val vm : WorkerProfileViewModel = viewModel(factory = WorkerProfileViewModel.provideFactory(
                    openResumeDetailsCallback = { resumeId ->
                        navController.navigate("$RESUME_DETAILS_BASE/$resumeId")
                        onNavigationForward("$RESUME_DETAILS_BASE/$resumeId")
                    },
                    createResumeCallback = {
                        navController.navigate(RESUME_CREATION)
                        onNavigationForward(RESUME_CREATION)
                    }
                ))

                WorkerProfileScreen(
                    state = vm.viewState,
                    loadResumes = { vm.processIntent(WorkerProfileContract.Intent.LoadResumes) },
                    openResumeDetails = { resumeId ->
                        vm.processIntent(WorkerProfileContract.Intent.OpenResumeDetails(resumeId))
                    },
                    openResumeEdit = {
                        vm.processIntent(WorkerProfileContract.Intent.CreateResume)
                    }
                )
            }

            UserRole.Employer -> {
                val vm : EmployerProfileViewModel = viewModel(factory = EmployerProfileViewModel.provideFactory(
                    openVacancyDetailsCallback = { vacancyId ->
                        navController.navigate("$VACANCY_DETAILS_BASE/$vacancyId")
                        onNavigationForward("$VACANCY_DETAILS_BASE/$vacancyId")
                    },
                    createVacancyCallback = {
                        navController.navigate(VACANCY_CREATION)
                        onNavigationForward(VACANCY_CREATION)
                    }
                ))

                EmployerProfileScreen(
                    state = vm.viewState,
                    loadVacancies = { vm.processIntent(EmployerProfileContract.Intent.LoadVacancies) },
                    openVacancyDetails = { vacancyId ->
                        vm.processIntent(EmployerProfileContract.Intent.OpenVacancyDetails(vacancyId))
                    },
                    openVacancyEdit = {
                        vm.processIntent(EmployerProfileContract.Intent.CreateVacancy)
                    }
                )
            }

            UserRole.Undefined -> LoadingComponent(
                modifier = Modifier.fillMaxSize(),
                description = "Logging out"
            )
        }
    }
    composable(SETTINGS) {
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = true
        }
        OnBackPressedWithSuper(onNavigateBack)

        val viewModel: SettingsViewModel = viewModel()
        SettingsScreen(
            state = viewModel.viewState,
            onUsernameFieldEdited = { viewModel.processIntent(SettingsContract.Intent.EditUsername(it)) },
            onEmailFieldEdited = { viewModel.processIntent(SettingsContract.Intent.EditEmail(it)) },
            onPasswordFieldEdited = { viewModel.processIntent(SettingsContract.Intent.EditPassword(it)) },
            onShowPasswordDialog = { viewModel.processIntent(SettingsContract.Intent.ShowPasswordDialog) },
            onPasswordDialogDismissed = { viewModel.processIntent(SettingsContract.Intent.HidePasswordDialog) },
            onPasswordDialogConfirmed = { viewModel.processIntent(SettingsContract.Intent.CheckPassword(it)) },
            onSaveChangesClicked = { viewModel.processIntent(SettingsContract.Intent.ShowAreYouSureDialog) },
            onAreYouSureDialogConfirmed = { viewModel.processIntent(SettingsContract.Intent.CommitChanges) },
            onAreYouSureDialogDismissed = { viewModel.processIntent(SettingsContract.Intent.HideAreYouSureDialog) }
        )
    }
}