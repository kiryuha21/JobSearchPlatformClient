package com.kiryuha21.jobsearchplatformclient

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kiryuha21.jobsearchplatformclient.ui.screens.VacancyEditScreen
import com.kiryuha21.jobsearchplatformclient.util.PreviewObjects
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VacancyEditTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setVacancyEdit() {
        composeTestRule.setContent {
            VacancyEditScreen(
                initVacancy = PreviewObjects.previewVacancy2,
                isLoading = false,
                loadingText = "",
                onUpdateVacancy = { _, _ -> }
            )
        }
    }

    private fun checkMinSalary(input: String, shouldBeValid: Boolean) {
        setVacancyEdit()

        composeTestRule.onNodeWithTag("vacancy_edit_min_salary").performTextClearance()
        composeTestRule.onNodeWithTag("vacancy_edit_min_salary").performTextInput(input)
        Espresso.closeSoftKeyboard()

        if (shouldBeValid) {
            composeTestRule.onNodeWithTag("enabled").assertIsDisplayed()
        } else {
            composeTestRule.onNodeWithTag("disabled").assertIsDisplayed()
        }
    }

    @Test
    fun negativeSalaryIsInvalid() {
        checkMinSalary("-10", false)
    }

    @Test
    fun alphabeticSalaryStringIsInvalid() {
        checkMinSalary("abcde", false)
    }

    @Test
    fun zeroSalaryIsValid() {
        checkMinSalary("0", true)
    }

    @Test
    fun positiveSalaryIsValid() {
        checkMinSalary("100000", true)
    }
}