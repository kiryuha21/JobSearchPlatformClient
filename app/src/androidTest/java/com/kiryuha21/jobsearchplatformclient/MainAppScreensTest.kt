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
import com.kiryuha21.jobsearchplatformclient.data.domain.User
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainUser
import com.kiryuha21.jobsearchplatformclient.di.AuthToken
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject
import com.kiryuha21.jobsearchplatformclient.data.remote.api.UserAPI
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.UserDTO
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationController
import com.kiryuha21.jobsearchplatformclient.ui.theme.JobSearchPlatformClientTheme
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainAppScreensTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var user: User
    private val userRetrofit = RetrofitObject.retrofit.create(UserAPI::class.java)

    @Before
    fun setUp() = runTest {
        val userDTO = UserDTO.SignUpUserDTO(
            email = "test@gmail.com",
            username = "my_example_login",
            password = "secure_one",
            role = UserRole.Worker
        )

        user = userRetrofit.createNewUser(userDTO).toDomainUser()
    }

    @After
    fun tearDown() = runTest {
        AuthToken.createToken("my_example_login", "secure_one")
        userRetrofit.deleteUserByUsername("my_example_login", "Bearer ${AuthToken.getToken()}")
    }

    @Test
    fun loginWorks() {
        composeTestRule.setContent {
            JobSearchPlatformClientTheme {
                NavigationController()
            }
        }

        composeTestRule.onNodeWithTag("login_username").performTextInput("my_example_login")
        composeTestRule.onNodeWithTag("login_password").performTextInput("secure_one")
        composeTestRule.onNodeWithText("Войти").performClick()

        composeTestRule.waitUntil(5000L) {
            return@waitUntil composeTestRule.onNodeWithContentDescription(
                "menu"
            ).isDisplayed()
        }
        composeTestRule.onNodeWithContentDescription("menu").assertIsDisplayed()
    }
}