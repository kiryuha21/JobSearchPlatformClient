package com.kiryuha21.jobsearchplatformclient

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.local.datastore.AppDataStore
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.ui.contract.SettingsContract
import com.kiryuha21.jobsearchplatformclient.ui.screens.SettingsScreen
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.SettingsViewModel
import com.kiryuha21.jobsearchplatformclient.util.FakeData
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsScreenTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun settingsAreDisabledOnStart() {
        composeTestRule.setContent {
            val vm: SettingsViewModel = viewModel(factory = SettingsViewModel.provideFactory(
                notificationInfoProvider = AppDataStore(LocalContext.current),
                logOut = {}
            ))

            SettingsScreen(
                state = vm.viewState,
                onUsernameFieldEdited = {},
                onEmailFieldEdited = {},
                onPasswordFieldEdited = {},
                onShowPasswordDialog = {},
                onPasswordDialogDismissed = {},
                onPasswordDialogConfirmed = {},
                showAreYouSureDialog = {},
                onAreYouSureDialogConfirmed = {},
                onAreYouSureDialogDismissed = {},
                toggleNotifications = {}
            )
        }

        composeTestRule.onNodeWithTag("settings_username").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("settings_email").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("settings_password").assertIsNotEnabled()
    }

    @Test
    fun settingsAreEnabledAfterCorrectPassword() {
        composeTestRule.setContent {
            val vm: SettingsViewModel = viewModel(factory = SettingsViewModel.provideFactory(
                notificationInfoProvider = AppDataStore(LocalContext.current),
                logOut = {}
            ))

            SettingsScreen(
                state = vm.viewState,
                onUsernameFieldEdited = {},
                onEmailFieldEdited = {},
                onPasswordFieldEdited = {},
                onShowPasswordDialog = { vm.processIntent(SettingsContract.Intent.ShowPasswordDialog) },
                onPasswordDialogDismissed = { vm.processIntent(SettingsContract.Intent.HidePasswordDialog) },
                onPasswordDialogConfirmed = { vm.processIntent(SettingsContract.Intent.CheckPassword(it)) },
                showAreYouSureDialog = {},
                onAreYouSureDialogConfirmed = {},
                onAreYouSureDialogDismissed = {},
                toggleNotifications = {}
            )
        }

        runTest {
            FakeData.withUser(UserRole.Worker) { _, user ->
                CurrentUser.loadUserInfo(user.username)

                composeTestRule.onNodeWithTag("settings_show_form_button").performClick()
                composeTestRule.onNodeWithTag("settings_confirm_password").performTextInput(user.password)
                composeTestRule.onNodeWithTag("settings_password_form_button").performClick()

                composeTestRule.onNodeWithTag("settings_username").assertIsEnabled()
                composeTestRule.onNodeWithTag("settings_email").assertIsEnabled()
                composeTestRule.onNodeWithTag("settings_password").assertIsEnabled()
            }
        }
    }
}