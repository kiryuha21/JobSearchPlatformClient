package com.kiryuha21.jobsearchplatformclient.ui.navigation

import android.graphics.Bitmap
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.domain.VacancyFilter
import com.kiryuha21.jobsearchplatformclient.data.local.datastore.TokenDataStore
import com.kiryuha21.jobsearchplatformclient.ui.components.OnBackPressedWithSuper
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.MainAppContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.ResumeDetailsContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerHomeContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerProfileContract
import com.kiryuha21.jobsearchplatformclient.ui.screens.EmployerHomeScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.EmployerProfileScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.LogInScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResetPasswordScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResumeDetailsScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResumeEditScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.SettingsScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.SignUpScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.VacancyDetailsScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.VacancyEditScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.WorkerHomeScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.WorkerProfileScreen
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.MainAppViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.NavigationViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ResumeDetailsViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.WorkerHomeViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.WorkerProfileViewModel

fun NavGraphBuilder.addAuthentication(
    navController: NavController,
    shouldShowAppBar: MutableState<Boolean>
) = with(NavigationGraph.Authentication) {
    composable(LOG_IN) {
        LaunchedEffect(key1 = Unit) {
            shouldShowAppBar.value = false
        }

        val viewModel: AuthViewModel = it.sharedAuthViewModel(
            navController,
            TokenDataStore(LocalContext.current)
        )

        LaunchedEffect(key1 = Unit) {
            viewModel.processIntent(AuthContract.AuthIntent.CheckRefreshToken)
        }

        LogInScreen(
            state = viewModel.viewState,
            onUsernameFieldEdited = { newLogin ->
                viewModel.processIntent(AuthContract.AuthIntent.EditLogin(newLogin))
            },
            onPasswordFieldEdited = { newPassword ->
                viewModel.processIntent(AuthContract.AuthIntent.EditPassword(newPassword))
            },
            onResetPassword = { viewModel.processIntent(AuthContract.AuthIntent.NavigateToResetPassword) },
            onSignUp = { viewModel.processIntent(AuthContract.AuthIntent.NavigateToSignUp) },
            onLogin = { viewModel.processIntent(AuthContract.AuthIntent.LogIn) },
            onErrorFix = { viewModel.processIntent(AuthContract.AuthIntent.FixError) }
        )
    }
    composable(SIGN_UP) {
        val viewModel: AuthViewModel = it.sharedAuthViewModel(
            navController,
            TokenDataStore(LocalContext.current)
        )

        SignUpScreen(
            state = viewModel.viewState,
            onLoginFieldUpdated = { newLogin ->
                viewModel.processIntent(AuthContract.AuthIntent.EditLogin(newLogin))
            },
            onEmailFieldUpdated = { newEmail ->
                viewModel.processIntent(AuthContract.AuthIntent.EditEmail(newEmail))
            },
            onPasswordFieldUpdated = { newPassword ->
                viewModel.processIntent(AuthContract.AuthIntent.EditPassword(newPassword))
            },
            onPasswordRepeatFieldUpdated = { newPasswordRepeat ->
                viewModel.processIntent(AuthContract.AuthIntent.EditPasswordRepeat(newPasswordRepeat))
            },
            onRoleToggled = { toggleElement ->
                viewModel.processIntent(AuthContract.AuthIntent.EditRole(toggleElement.role))
            },
            onErrorFix = { viewModel.processIntent(AuthContract.AuthIntent.FixError) },
            onRegister = { viewModel.processIntent(AuthContract.AuthIntent.SignUp) })
    }
    composable(RESET_PASSWORD) {
        val viewModel: AuthViewModel = it.sharedAuthViewModel(
            navController,
            TokenDataStore(LocalContext.current)
        )

        ResetPasswordScreen(
            state = viewModel.viewState,
            onEmailFieldEdited = { newEmail ->
                viewModel.processIntent(AuthContract.AuthIntent.EditEmail(newEmail))
            },
            onReset = { viewModel.processIntent(AuthContract.AuthIntent.ResetPassword) }
        )
    }
}

fun NavGraphBuilder.addMainApp(
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
                    openVacancyCallback = { navController.navigate("$VACANCY_DETAILS_BASE/$it") }
                )

                WorkerHomeScreen(
                    state = vm.viewState,
                    loadVacancies = { vm.processIntent(WorkerHomeContract.Intent.LoadVacancies(VacancyFilter())) }, // TODO: should be a parameter of callback
                    openVacancyDetails = { vacancyId ->
                        vm.processIntent(WorkerHomeContract.Intent.OpenVacancyDetails(vacancyId))
                        onNavigationForward("$VACANCY_DETAILS_BASE/$vacancyId")
                    }
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
                val vm : WorkerProfileViewModel = backStack.sharedWorkerProfileViewModel(
                    navController = navController,
                    openResumeDetailsCallback = { navController.navigate("$RESUME_DETAILS_BASE/$it") },
                    createResumeCallback = { navController.navigate(RESUME_EDIT) }
                )

                WorkerProfileScreen(
                    state = vm.viewState,
                    loadResumes = { vm.processIntent(WorkerProfileContract.Intent.LoadResumes) },
                    openResumeDetails = { resumeId ->
                        vm.processIntent(WorkerProfileContract.Intent.OpenResumeDetails(resumeId))
                        onNavigationForward("$RESUME_DETAILS_BASE/$resumeId")
                    },
                    openResumeEdit = {
                        vm.processIntent(WorkerProfileContract.Intent.CreateResume)
                        onNavigationForward(RESUME_EDIT)
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
    composable(VACANCY_DETAILS) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = false
        }
        OnBackPressedWithSuper(onNavigateBack)

        val viewModel: MainAppViewModel = backStack.sharedMainAppViewModel(navController)

        VacancyDetailsScreen(
            editable = CurrentUser.info.role == UserRole.Employer,
            vacancyId = backStack.arguments?.getString("vacancyId"),
            state = viewModel.viewState
        )
    }
    composable(RESUME_DETAILS) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = false
        }
        OnBackPressedWithSuper(onNavigateBack)

        val viewModel: ResumeDetailsViewModel = backStack.sharedResumeDetailsViewModel(
            navController = navController,
            navigateCallback = { navController.navigate(PROFILE) },
            navigateToEdit = { navController.navigate(RESUME_EDIT) },
            navigateWithPopCallback = {
                navController.popBackStack()
                navController.navigate(PROFILE)
            }
        )

        val resumeId = backStack.arguments?.getString("resumeId") ?: throw Exception("resumeId should be passed via backstack!")
        LaunchedEffect(Unit) {
            viewModel.processIntent(ResumeDetailsContract.Intent.LoadResume(resumeId))
        }

        ResumeDetailsScreen(
            editable = CurrentUser.info.role == UserRole.Worker,
            onEdit = {
                viewModel.processIntent(ResumeDetailsContract.Intent.OpenEdit(it))
                onNavigationForward(RESUME_EDIT)
            },
            onDelete = {
                viewModel.processIntent(ResumeDetailsContract.Intent.DeleteResume(it.id))
                onNavigationForward(PROFILE)
            },
            state = viewModel.viewState
        )
    }
    composable(RESUME_EDIT) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = false
        }
        OnBackPressedWithSuper(onNavigateBack)

        val viewModel: ResumeDetailsViewModel = backStack.sharedResumeDetailsViewModel(
            navController = navController,
            navigateCallback = { navController.navigate(PROFILE) },
            navigateToEdit = { navController.navigate(RESUME_EDIT) },
            navigateWithPopCallback = {
                navController.popBackStack()
                navController.navigate(PROFILE)
            }
        )

        val resume = viewModel.viewState.openedResume ?: Resume()
        val onUpdateResume: (Resume, Bitmap?) -> Unit = if (resume.id.isNotEmpty()) {
            { res, bitmap -> viewModel.processIntent(ResumeDetailsContract.Intent.EditResume(res, bitmap)) }
        } else {
            { res, bitmap -> viewModel.processIntent(ResumeDetailsContract.Intent.CreateResume(res, bitmap)) }
        }

        ResumeEditScreen(
            initResume = viewModel.viewState.openedResume ?: Resume(),
            isLoading = viewModel.viewState.isLoading,
            onUpdateResume = { updatedResume, bitmap ->
                onUpdateResume(updatedResume, bitmap)
                onNavigationForward(PROFILE)
            }
        )
    }
    composable(VACANCY_EDIT) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = false
        }
        OnBackPressedWithSuper(onNavigateBack)

        val viewModel: MainAppViewModel = backStack.sharedMainAppViewModel(navController)
        val vacancy = viewModel.viewState.openedVacancy!!

        val onUpdateVacancy: (Vacancy, Bitmap?) -> Unit = if (vacancy.id.isNotEmpty()) {
            { vac, bitmap -> viewModel.processIntent(MainAppContract.MainAppIntent.EditVacancy(vac, bitmap)) }
        } else {
            { vac, bitmap -> viewModel.processIntent(MainAppContract.MainAppIntent.CreateNewVacancy(vac, bitmap)) }
        }

        VacancyEditScreen(
            initVacancy = viewModel.viewState.openedVacancy!!,
            onUpdateVacancy = { updatedVacancy, bitmap ->
                onUpdateVacancy(updatedVacancy, bitmap)
                onNavigationForward(PROFILE)
            }
        )
    }
}

@Composable
fun NavigationController() {
    val navController = rememberNavController()
    val shouldShowTopBar = rememberSaveable { mutableStateOf(false) }

    val ctx = LocalContext.current
    val navigationVM: NavigationViewModel = viewModel(
        factory = NavigationViewModel.provideFactory(TokenDataStore(ctx)) {
            navController.navigate(NavigationGraph.Authentication.LOG_IN) {
                popUpTo(NavigationGraph.MainApp.NAV_ROUTE) {
                    inclusive = true
                }
            }
        }
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
                addMainApp(
                    navController = navController,
                    shouldShowAppBar = shouldShowTopBar,
                    onNavigateBack = { navigationVM.navigateBack() },
                    onNavigationForward = { navigationVM.navigateTo(it) }
                )
            }
        }
    }
}