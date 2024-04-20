package com.kiryuha21.jobsearchplatformclient.ui.navigation

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
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
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.MainAppContract
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
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.MainAppViewModel

fun NavGraphBuilder.addAuthentication(
    navController: NavController,
    shouldShowAppBar: MutableState<Boolean>
) = with(NavigationGraph.Authentication) {
    composable(LOG_IN) {
        LaunchedEffect(key1 = Unit) {
            shouldShowAppBar.value = false
        }

        val viewModel: AuthViewModel = it.sharedAuthViewModel(navController)

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
        val viewModel: AuthViewModel = it.sharedAuthViewModel(navController)

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
        val viewModel: AuthViewModel = it.sharedAuthViewModel(navController)

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
    shouldShowAppBar: MutableState<Boolean>
) = with(NavigationGraph.MainApp) {
    composable(HOME_SCREEN) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = true
        }

        val viewModel: MainAppViewModel = backStack.sharedMainAppViewModel(navController)

        when (CurrentUser.info.role) {
            UserRole.Worker -> WorkerHomeScreen(
                state = viewModel.viewState,
                loadVacancies = { viewModel.processIntent(MainAppContract.MainAppIntent.FindMatchingVacancies) },
                openVacancyDetails = {viewModel.processIntent(MainAppContract.MainAppIntent.OpenVacancyDetails(it)) }
            )

            UserRole.Employer -> EmployerHomeScreen(
                state = viewModel.viewState,
                loadResumes = { viewModel.processIntent(MainAppContract.MainAppIntent.FindMatchingResumes) },
                openResumeDetails = { viewModel.processIntent(MainAppContract.MainAppIntent.OpenResumeDetails(it)) }
            )
        }
    }
    composable(PROFILE) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = true
        }

        val viewModel: MainAppViewModel = backStack.sharedMainAppViewModel(navController)

        when (CurrentUser.info.role) {
            UserRole.Worker -> WorkerProfileScreen(
                state = viewModel.viewState,
                loadResumes = { viewModel.processIntent(MainAppContract.MainAppIntent.LoadProfileResumes) },
                openResumeDetails = { viewModel.processIntent(MainAppContract.MainAppIntent.OpenResumeDetails(it)) },
                openResumeEdit = { viewModel.processIntent(MainAppContract.MainAppIntent.OpenResumeEdit(Resume())) }
            )

            UserRole.Employer -> EmployerProfileScreen(
                state = viewModel.viewState,
                loadVacancies = { viewModel.processIntent(MainAppContract.MainAppIntent.LoadProfileVacancies) },
                openVacancyDetails = { viewModel.processIntent(MainAppContract.MainAppIntent.OpenVacancyDetails(it)) },
                openVacancyEdit = { viewModel.processIntent(MainAppContract.MainAppIntent.OpenVacancyEdit(
                    Vacancy()
                )) }
            )
        }
    }
    composable(SETTINGS) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = true
        }

        val viewModel: MainAppViewModel = backStack.sharedMainAppViewModel(navController)
        SettingsScreen({}, {}, {})
    }
    composable(VACANCY_DETAILS) { backStack ->
        LaunchedEffect(Unit) {
            shouldShowAppBar.value = false
        }

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

        val viewModel: MainAppViewModel = backStack.sharedMainAppViewModel(navController)
        ResumeDetailsScreen(
            editable = CurrentUser.info.role == UserRole.Worker,
            resumeId = backStack.arguments?.getString("resumeId"),
            onEdit = { viewModel.processIntent(MainAppContract.MainAppIntent.OpenResumeEdit(it)) },
            onDelete = { viewModel.processIntent(MainAppContract.MainAppIntent.DeleteResume(it)) },
            state = viewModel.viewState
        )
    }
    composable(RESUME_EDIT) { backStack ->
        val viewModel: MainAppViewModel = backStack.sharedMainAppViewModel(navController)
        val resume = viewModel.viewState.openedResume!!

        val onUpdateResume: (Resume, Bitmap?) -> Unit = if (resume.id.isNotEmpty()) {
            { res, bitmap -> viewModel.processIntent(MainAppContract.MainAppIntent.EditResume(res, bitmap)) }
        } else {
            { res, bitmap -> viewModel.processIntent(MainAppContract.MainAppIntent.CreateNewResume(res, bitmap)) }
        }

        ResumeEditScreen(
            initResume = viewModel.viewState.openedResume!!,
            isLoading = viewModel.viewState.isLoading,
            onUpdateResume = onUpdateResume
        )
    }
    composable(VACANCY_EDIT) { backStack ->
        val viewModel: MainAppViewModel = backStack.sharedMainAppViewModel(navController)
        val vacancy = viewModel.viewState.openedVacancy!!

        val onUpdateVacancy: (Vacancy, Bitmap?) -> Unit = if (vacancy.id.isNotEmpty()) {
            { vac, bitmap -> viewModel.processIntent(MainAppContract.MainAppIntent.EditVacancy(vac, bitmap)) }
        } else {
            { vac, bitmap -> viewModel.processIntent(MainAppContract.MainAppIntent.CreateNewVacancy(vac, bitmap)) }
        }

        VacancyEditScreen(
            initVacancy = viewModel.viewState.openedVacancy!!,
            onUpdateVacancy = onUpdateVacancy
        )
    }
}

@Composable
fun NavigationController() {
    val navController = rememberNavController()
    val shouldShowTopBar = rememberSaveable { mutableStateOf(false) }

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