package com.kiryuha21.jobsearchplatformclient

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kiryuha21.jobsearchplatformclient.data.domain.MIN_PASSWORD_LENGTH
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationController
import com.kiryuha21.jobsearchplatformclient.ui.theme.JobSearchPlatformClientTheme
import com.kiryuha21.jobsearchplatformclient.util.FakeData
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AuthScreensTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.kiryuha21.jobsearchplatformclient", appContext.packageName)
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun openSignUp() {
        composeTestRule.setContent {
            JobSearchPlatformClientTheme {
                NavigationController()
            }
        }

        composeTestRule.onNodeWithText("Зарегистрироваться").performClick()
    }

    @Test
    fun testStartPageIsLoginPage() {
        composeTestRule.setContent {
            JobSearchPlatformClientTheme {
                NavigationController()
            }
        }

        composeTestRule.onNodeWithText("Войти").assertIsDisplayed()
    }

    @Test
    fun testNavigationToResetPasswordWorks() {
        composeTestRule.setContent {
            JobSearchPlatformClientTheme {
                NavigationController()
            }
        }

        composeTestRule.onNodeWithText("Восстановить пароль").performClick()
        composeTestRule.onNodeWithText("Восстановление пароля").assertIsDisplayed()
    }

    @Test
    fun testNavigationToSignUpWorks() {
        composeTestRule.setContent {
            JobSearchPlatformClientTheme {
                NavigationController()
            }
        }

        composeTestRule.onNodeWithText("Зарегистрироваться").performClick()
        composeTestRule.onNodeWithText("Регистрация").assertIsDisplayed()
    }

    @Test
    fun loginPageWorks() {
        composeTestRule.setContent {
            JobSearchPlatformClientTheme {
                NavigationController()
            }
        }

        composeTestRule.onNodeWithTag("login_username").performTextInput("username")
        composeTestRule.onNodeWithTag("login_username").assert(hasText("username"))

        composeTestRule.onNodeWithContentDescription("Показать пароль").performClick()
        composeTestRule.onNodeWithTag("login_password").performTextInput("password")
        composeTestRule.onNodeWithTag("login_password").assert(hasText("password"))
    }

    @Test
    fun invalidPasswordNotWorks() {
        composeTestRule.setContent {
            JobSearchPlatformClientTheme {
                NavigationController()
            }
        }

        composeTestRule.onNodeWithText("Войти").performClick()
        composeTestRule.waitUntil { composeTestRule.onNodeWithTag("loading").isNotDisplayed() }
        composeTestRule.onNodeWithTag("error").assertIsDisplayed()
    }

    @Test
    fun notMatchingPasswordsNotWork() {
        openSignUp()

        composeTestRule.onNodeWithTag("signup_password").performTextInput("password1")
        composeTestRule.onNodeWithTag("signup_password_repeat").performTextInput("password2")
        composeTestRule.onNodeWithTag("signup_button").assertIsNotEnabled()
    }

    @Test
    fun tooSmallPasswordDoesntWork() {
        openSignUp()

        composeTestRule.onNodeWithTag("signup_password").performTextInput("")
        composeTestRule.onNodeWithTag("signup_password_repeat").performTextInput("")
        composeTestRule.onNodeWithTag("signup_button").assertIsNotEnabled()
    }

    @Test
    fun exactNeededLengthPasswordWorks() {
        openSignUp()

        val password = FakeData.randomString(MIN_PASSWORD_LENGTH)

        composeTestRule.onNodeWithTag("signup_password").performTextInput(password)
        composeTestRule.onNodeWithTag("signup_password_repeat").performTextInput(password)
        composeTestRule.onNodeWithTag("signup_button").assertIsEnabled()
    }

    @Test
    fun hugePasswordWorks() {
        openSignUp()

        val password = FakeData.randomString(40)

        composeTestRule.onNodeWithTag("signup_password").performTextInput(password)
        composeTestRule.onNodeWithTag("signup_password_repeat").performTextInput(password)
        composeTestRule.onNodeWithTag("signup_button").assertIsEnabled()
    }
}