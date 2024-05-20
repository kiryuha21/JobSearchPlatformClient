package com.kiryuha21.jobsearchplatformclient

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationController
import com.kiryuha21.jobsearchplatformclient.ui.theme.JobSearchPlatformClientTheme
import com.kiryuha21.jobsearchplatformclient.util.FakeData
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginWorks() {
        composeTestRule.setContent {
            JobSearchPlatformClientTheme {
                NavigationController()
            }
        }

        runTest {
            FakeData.withUser(UserRole.Worker) { _, user ->
                composeTestRule.onNodeWithTag("login_username").performTextInput(user.username)
                composeTestRule.onNodeWithTag("login_password").performTextInput(user.password)
                composeTestRule.onNodeWithText("Войти").performClick()

                composeTestRule.waitUntil(5000L) {
                    composeTestRule.onNodeWithContentDescription(
                        "menu"
                    ).isDisplayed()
                }
                composeTestRule.onNodeWithContentDescription("menu").assertIsDisplayed()
            }
        }
    }
}