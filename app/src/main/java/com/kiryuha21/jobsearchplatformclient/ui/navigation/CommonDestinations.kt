package com.kiryuha21.jobsearchplatformclient.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.local.AppDatabase
import com.kiryuha21.jobsearchplatformclient.data.local.datastore.AppDataStore
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.special.OnBackPressedWithSuper
import com.kiryuha21.jobsearchplatformclient.ui.contract.EmployerHomeContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.EmployerProfileContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.JobApplicationContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.OfferContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.ResponseContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.SettingsContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerHomeContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerProfileContract
import com.kiryuha21.jobsearchplatformclient.ui.screens.EmployerHomeScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.EmployerProfileScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.JobApplicationScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.OfferCreationScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResponseCreationScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.SettingsScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.WorkerHomeScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.WorkerProfileScreen
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.EmployerHomeViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.EmployerProfileViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.JobApplicationViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.OfferViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ResponseViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.SettingsViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.WorkerHomeViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.WorkerProfileViewModel

fun NavGraphBuilder.addCommonDestinations(
    navController: NavController,
    shouldShowAppBar: MutableState<Boolean>,
    onNavigateBack: () -> Unit,
    onNavigationForward: (String) -> Unit,
    logOut: () -> Unit
) = with(NavigationGraph.MainApp) {
    composable(HOME_SCREEN) {
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = true
        }
        OnBackPressedWithSuper(onNavigateBack)

        when (CurrentUser.info.role) {
            UserRole.Worker -> {
                val vm : WorkerHomeViewModel = viewModel(factory = WorkerHomeViewModel.provideFactory(
                    openVacancyCallback = { vacancyId ->
                        navController.navigate("$VACANCY_DETAILS_BASE/$vacancyId")
                        onNavigationForward("$VACANCY_DETAILS_BASE/$vacancyId")
                    },
                    vacancyDAO = AppDatabase.getDatabase(LocalContext.current).vacancyDAO
                ))

                WorkerHomeScreen(
                    state = vm.viewState,
                    openVacancyDetails = { vacancyId ->
                        vm.processIntent(WorkerHomeContract.Intent.OpenVacancyDetails(vacancyId))
                    },
                    loadRecommended = { page ->
                        vm.processIntent(WorkerHomeContract.Intent.LoadRecommendations(page))
                    },
                    loadFiltered = { filters ->
                        vm.processIntent(WorkerHomeContract.Intent.LoadVacancies(filters))
                    },
                    switchToOnlineRecommendations = {
                        vm.processIntent(WorkerHomeContract.Intent.SwitchToOnlineRecommendations)
                    },
                    refreshRecommendations = {
                        vm.processIntent(WorkerHomeContract.Intent.RefreshRecommendations)
                    }
                )
            }

            UserRole.Employer -> {
                val vm : EmployerHomeViewModel = viewModel(factory = EmployerHomeViewModel.provideFactory(
                    openResumeCallback = { resumeId ->
                        navController.navigate("$RESUME_DETAILS_BASE/$resumeId")
                        onNavigationForward("$RESUME_DETAILS_BASE/$resumeId")
                    },
                    resumeDAO = AppDatabase.getDatabase(LocalContext.current).resumeDAO
                ))

                EmployerHomeScreen(
                    state = vm.viewState,
                    openResumeDetails = { resumeId ->
                        vm.processIntent(EmployerHomeContract.Intent.OpenResumeDetails(resumeId))
                    },
                    loadFiltered = { filters ->
                        vm.processIntent(EmployerHomeContract.Intent.LoadResumes(filters))
                    },
                    loadRecommended = { page ->
                        vm.processIntent(EmployerHomeContract.Intent.LoadRecommendations(page))
                    },
                    switchToOnlineRecommendations = {
                        vm.processIntent(EmployerHomeContract.Intent.SwitchToOnlineRecommendations)
                    },
                    refreshRecommendations = {
                        vm.processIntent(EmployerHomeContract.Intent.RefreshPage)
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

        val viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.provideFactory(
            notificationInfoProvider = AppDataStore(LocalContext.current),
            logOut = logOut
        ))
        SettingsScreen(
            state = viewModel.viewState,
            onUsernameFieldEdited = { viewModel.processIntent(SettingsContract.Intent.EditUsername(it)) },
            onEmailFieldEdited = { viewModel.processIntent(SettingsContract.Intent.EditEmail(it)) },
            onPasswordFieldEdited = { viewModel.processIntent(SettingsContract.Intent.EditPassword(it)) },
            onShowPasswordDialog = { viewModel.processIntent(SettingsContract.Intent.ShowPasswordDialog) },
            onPasswordDialogDismissed = { viewModel.processIntent(SettingsContract.Intent.HidePasswordDialog) },
            onPasswordDialogConfirmed = { viewModel.processIntent(SettingsContract.Intent.CheckPassword(it)) },
            showAreYouSureDialog = { viewModel.processIntent(SettingsContract.Intent.ShowAreYouSureDialog(it)) },
            onAreYouSureDialogConfirmed = { viewModel.processIntent(SettingsContract.Intent.CommitChanges) },
            onAreYouSureDialogDismissed = { viewModel.processIntent(SettingsContract.Intent.HideAreYouSureDialog) },
            toggleNotifications = { viewModel.processIntent(SettingsContract.Intent.ToggleNotifications(it)) }
        )
    }
    composable(JOB_APPLICATIONS) {
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = true
        }
        OnBackPressedWithSuper(onNavigateBack)

        val vm: JobApplicationViewModel = viewModel(factory = JobApplicationViewModel.provideFactory(
            navigateToVacancyDetails = { vacancyId ->
                navController.navigate("$VACANCY_DETAILS_BASE/$vacancyId")
                onNavigationForward("$VACANCY_DETAILS_BASE/$vacancyId")
            },
            navigateToResumeDetails = { resumeId ->
                navController.navigate("$RESUME_DETAILS_BASE/$resumeId")
                onNavigationForward("$RESUME_DETAILS_BASE/$resumeId")
            }
        ))

        JobApplicationScreen(
            state = vm.viewState,
            showVacancyDetails = {
                vm.processIntent(JobApplicationContract.Intent.ShowVacancyDetails(it))
            },
            showResumeDetails = {
                vm.processIntent(JobApplicationContract.Intent.ShowResumeDetails(it))
            },
            markChecked = {
                vm.processIntent(JobApplicationContract.Intent.MarkSeen(it))
            }
        )
    }
    composable(OFFER_CREATION) {backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = false
        }
        OnBackPressedWithSuper(onNavigateBack)

        val resumeId = backStack.arguments?.getString("resumeId") ?: throw Exception("resumeId should be passed via backstack!")

        val ctx = LocalContext.current
        val vm: OfferViewModel = viewModel(factory = OfferViewModel.provideFactory(
            resumeId = resumeId,
            navigateBackToResume = {
                navController.popBackStack()
                onNavigateBack()
            },
            showToast = { text ->
                Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show()
            }
        ))

        OfferCreationScreen(
            selectVacancy = { vm.processIntent(OfferContract.Intent.ChooseVacancy(it)) },
            updateText = { vm.processIntent(OfferContract.Intent.UpdateMessage(it)) },
            setStage = { vm.processIntent(OfferContract.Intent.SetOfferStage(it)) },
            saveOffer = { vm.processIntent(OfferContract.Intent.CreateOffer) },
            state = vm.viewState
        )
    }
    composable(RESPONSE_CREATION) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = false
        }
        OnBackPressedWithSuper(onNavigateBack)

        val vacancyId = backStack.arguments?.getString("vacancyId") ?: throw Exception("vacancyId should be passed via backstack!")

        val ctx = LocalContext.current
        val vm: ResponseViewModel = viewModel(factory = ResponseViewModel.provideFactory(
            vacancyId = vacancyId,
            navigateBackToVacancy = {
                navController.popBackStack()
                onNavigateBack()
            },
            showToast = { text ->
                Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show()
            }
        ))

        ResponseCreationScreen(
            selectResume = { vm.processIntent(ResponseContract.Intent.ChooseResume(it)) },
            updateText = { vm.processIntent(ResponseContract.Intent.UpdateMessage(it)) },
            setStage = { vm.processIntent(ResponseContract.Intent.SetResponseStage(it)) },
            saveResponse = { vm.processIntent(ResponseContract.Intent.CreateResponse) },
            state = vm.viewState
        )
    }
}