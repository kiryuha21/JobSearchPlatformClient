package com.kiryuha21.jobsearchplatformclient.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.PublicationStatus
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.HomePageContract
import com.kiryuha21.jobsearchplatformclient.ui.screens.EmployerHomeScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.EmployerProfileScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.WorkerHomeScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.LogInScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.WorkerProfileScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResetPasswordScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResumeDetailsScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.ResumeEditScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.SettingsScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.SignUpScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.VacancyDetailsScreen
import com.kiryuha21.jobsearchplatformclient.ui.screens.VacancyEditScreen
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.AuthViewModel
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.HomePageViewModel

fun NavGraphBuilder.addAuthentication(
    navController: NavController,
    shouldShowAppBar: MutableState<Boolean>
) = with(NavigationGraph.Authentication) {
    composable(LOG_IN) {
        LaunchedEffect(key1 = Unit) {
            shouldShowAppBar.value = false
        }

        val viewModel: AuthViewModel = it.sharedAuthViewModel(navController)
        val state by viewModel.viewState

        LogInScreen(
            state = state,
            onUsernameFieldEdited = { newLogin ->
                viewModel.processIntent(AuthContract.AuthIntent.EditLogin(newLogin))
            },
            onPasswordFieldEdited = { newPassword ->
                viewModel.processIntent(AuthContract.AuthIntent.EditPassword(newPassword))
            },
            onResetPassword = { viewModel.processIntent(AuthContract.AuthIntent.NavigateToResetPassword) },
            onSignUp = { viewModel.processIntent(AuthContract.AuthIntent.NavigateToSignUp) },
            onLogin = { viewModel.processIntent(AuthContract.AuthIntent.LogIn) }
        )
    }
    composable(SIGN_UP) {
        val viewModel: AuthViewModel = it.sharedAuthViewModel(navController)
        val state by viewModel.viewState

        SignUpScreen(
            state = state,
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
        val viewModel: AuthViewModel = it.sharedAuthViewModel(navController)
        val state by viewModel.viewState

        ResetPasswordScreen(
            state = state,
            onEmailFieldEdited = { newEmail ->
                viewModel.processIntent(AuthContract.AuthIntent.EditEmail(newEmail))
            },
            onReset = { viewModel.processIntent(AuthContract.AuthIntent.ResetPassword) }
        )
    }
}

fun NavGraphBuilder.addMainApp(
    navController: NavController,
    shouldShowAppBar: MutableState<Boolean>
) = with(NavigationGraph.MainApp) {
    val user by CurrentUser.info

    composable(HOME_SCREEN) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = true
        }

        val viewModel: HomePageViewModel = backStack.sharedHomePageViewModel(navController)
        val state by viewModel.viewState

        when (user.role) {
            UserRole.Worker -> WorkerHomeScreen(
                state = state,
                loadVacancies = { viewModel.processIntent(HomePageContract.HomePageIntent.FindMatchingVacancies) },
                openVacancyDetails = {viewModel.processIntent(HomePageContract.HomePageIntent.OpenVacancyDetails(it)) }
            )

            UserRole.Employer -> EmployerHomeScreen(
                state = state,
                loadResumes = { viewModel.processIntent(HomePageContract.HomePageIntent.FindMatchingResumes) },
                openResumeDetails = { viewModel.processIntent(HomePageContract.HomePageIntent.OpenResumeDetails(it)) }
            )
        }
    }
    composable(PROFILE) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = true
        }

        val viewModel: HomePageViewModel = backStack.sharedHomePageViewModel(navController)
        val state by viewModel.viewState

        when (user.role) {
            UserRole.Worker -> WorkerProfileScreen(
                state = state,
                loadResumes = { viewModel.processIntent(HomePageContract.HomePageIntent.LoadProfileResumes) },
                openResumeDetails = { viewModel.processIntent(HomePageContract.HomePageIntent.OpenResumeDetails(it)) },
                openResumeEdit = { viewModel.processIntent(HomePageContract.HomePageIntent.OpenResumeEdit) }
            )

            UserRole.Employer -> EmployerProfileScreen(
                state = state,
                loadVacancies = { viewModel.processIntent(HomePageContract.HomePageIntent.LoadProfileVacancies) },
                openVacancyDetails = { viewModel.processIntent(HomePageContract.HomePageIntent.OpenVacancyDetails(it)) },
                openVacancyEdit = { viewModel.processIntent(HomePageContract.HomePageIntent.OpenVacancyEdit) }
            )
        }
    }
    composable(SETTINGS) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = true
        }

        val viewModel: HomePageViewModel = backStack.sharedHomePageViewModel(navController)
        SettingsScreen({}, {}, {})
    }
    composable(VACANCY_DETAILS) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = false
        }

        val viewModel: HomePageViewModel = backStack.sharedHomePageViewModel(navController)
        val state by viewModel.viewState

        VacancyDetailsScreen(
            editable = user.role == UserRole.Employer,
            vacancyId = backStack.arguments?.getString("vacancyId"),
            state = state
        )
    }
    composable(RESUME_DETAILS) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = false
        }

        val viewModel = backStack.sharedHomePageViewModel<HomePageViewModel>(navController)
        val state by viewModel.viewState
        ResumeDetailsScreen(
            editable = user.role == UserRole.Worker,
            resumeId = backStack.arguments?.getString("resumeId"),
            state = state
        )
    }
    composable(RESUME_EDIT) { backStack ->
        val viewModel = backStack.sharedHomePageViewModel<HomePageViewModel>(navController)

        ResumeEditScreen(
            initResume = Resume("", "", "", "", "", "", mutableListOf(), mutableListOf(), PublicationStatus.Published),
            onClick = { viewModel.processIntent(HomePageContract.HomePageIntent.CreateNewResume(it)) }
        )
    }
    composable(VACANCY_EDIT) { backStack ->
        val viewModel = backStack.sharedHomePageViewModel<HomePageViewModel>(navController)

        VacancyEditScreen(
            initVacancy = Vacancy("", "", "", Company(""), 0, 0, PublicationStatus.Published),
            onClick = { viewModel.processIntent(HomePageContract.HomePageIntent.CreateNewVacancy(it)) }
        )
    }
}

@Composable
fun NavigationController() {
    val navController = rememberNavController()
    val shouldShowTopBar = rememberSaveable { mutableStateOf(true) }

    MainAppScaffold(
        navigateFunction = navController::navigate,
        onLogOut = {
            navController.navigate(NavigationGraph.Authentication.LOG_IN) {
                popUpTo(NavigationGraph.MainApp.NAV_ROUTE) {
                    inclusive = true
                }
            }
        },
        shouldShowTopBar = shouldShowTopBar.value
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
                addMainApp(navController, shouldShowTopBar)
            }
        }
    }
}