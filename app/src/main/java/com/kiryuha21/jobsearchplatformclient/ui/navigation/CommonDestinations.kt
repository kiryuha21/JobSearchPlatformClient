package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.ResumeFilter
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.domain.VacancyFilter
import com.kiryuha21.jobsearchplatformclient.ui.components.OnBackPressedWithSuper
import com.kiryuha21.jobsearchplatformclient.ui.contract.EmployerHomeContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.EmployerProfileContract
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
                        vm.processIntent(WorkerHomeContract.Intent.LoadVacancies(VacancyFilter())) // TODO: real filter should be here
                    },
                    openVacancyDetails = {
                        vacancyId -> vm.processIntent(WorkerHomeContract.Intent.OpenVacancyDetails(vacancyId))
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
                        vm.processIntent(EmployerHomeContract.Intent.LoadResumes(ResumeFilter())) // TODO: real filter should be here
                    },
                    openResumeDetails = {
                        resumeId -> vm.processIntent(EmployerHomeContract.Intent.OpenResumeDetails(resumeId))
                    }
                )
            }
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
        }
    }
    composable(SETTINGS) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = true
        }
        OnBackPressedWithSuper(onNavigateBack)

        val viewModel: SettingsViewModel = viewModel()
        SettingsScreen({}, {}, {})
    }
}