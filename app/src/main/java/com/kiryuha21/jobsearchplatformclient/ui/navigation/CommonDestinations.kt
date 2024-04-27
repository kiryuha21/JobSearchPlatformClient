package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.domain.VacancyFilter
import com.kiryuha21.jobsearchplatformclient.ui.components.OnBackPressedWithSuper
import com.kiryuha21.jobsearchplatformclient.ui.contract.MainAppContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerHomeContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerProfileContract
import com.kiryuha21.jobsearchplatformclient.ui.screens.EmployerHomeScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.EmployerProfileScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.SettingsScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.WorkerHomeScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.WorkerProfileScreen
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.MainAppViewModel
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

        val viewModel: MainAppViewModel = backStack.sharedMainAppViewModel(navController)

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
                    loadVacancies = { vm.processIntent(
                        WorkerHomeContract.Intent.LoadVacancies(
                            VacancyFilter()
                        )) }, // TODO: should be a parameter of callback
                    openVacancyDetails = { vacancyId -> vm.processIntent(WorkerHomeContract.Intent.OpenVacancyDetails(vacancyId)) }
                )
            }

            UserRole.Employer -> EmployerHomeScreen(
                state = viewModel.viewState,
                loadResumes = { viewModel.processIntent(MainAppContract.MainAppIntent.FindMatchingResumes) },
                openResumeDetails = { resumeId ->
                    viewModel.processIntent(MainAppContract.MainAppIntent.OpenResumeDetails(resumeId))
                    onNavigationForward("$RESUME_DETAILS_BASE/$resumeId")
                }
            )
        }
    }
    composable(PROFILE) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = true
        }
        OnBackPressedWithSuper(onNavigateBack)

        val viewModel: MainAppViewModel = backStack.sharedMainAppViewModel(navController)

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

            UserRole.Employer -> EmployerProfileScreen(
                state = viewModel.viewState,
                loadVacancies = { viewModel.processIntent(MainAppContract.MainAppIntent.LoadProfileVacancies) },
                openVacancyDetails = { vacancyId ->
                    viewModel.processIntent(MainAppContract.MainAppIntent.OpenVacancyDetails(vacancyId))
                    onNavigationForward("$VACANCY_DETAILS_BASE/$vacancyId")
                },
                openVacancyEdit = {
                    viewModel.processIntent(MainAppContract.MainAppIntent.OpenVacancyEdit(Vacancy()))
                    onNavigationForward(VACANCY_EDIT)
                }
            )
        }
    }
    composable(SETTINGS) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = true
        }
        OnBackPressedWithSuper(onNavigateBack)

        val viewModel: MainAppViewModel = backStack.sharedMainAppViewModel(navController)
        SettingsScreen({}, {}, {})
    }
}