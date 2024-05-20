package com.kiryuha21.jobsearchplatformclient

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ResumeEditTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalMaterial3Api::class)
    private fun setDatePicker() {
        composeTestRule.setContent {
            DatePicker(state = rememberDatePickerState(initialDisplayMode = DisplayMode.Input))
        }
    }

    private fun checkCalendarDate(input: String, shouldBeValid: Boolean) {
        setDatePicker()

        composeTestRule.onNodeWithText("Date").performTextInput(input)
        Espresso.closeSoftKeyboard()

        if (shouldBeValid) {
            composeTestRule.onNodeWithText("Date out of expected year range", substring = true).assertIsNotDisplayed()
        } else {
            composeTestRule.onNodeWithText("Date out of expected year range", substring = true).assertIsDisplayed()
        }
    }

    @Test
    fun yearBefore1900IsInvalid() {
        checkCalendarDate("01011899", false)
    }

    @Test
    fun yearAfter2100IsInvalid() {
        checkCalendarDate("01012101", false)
    }

    @Test
    fun otherYearsAreValid() {
        checkCalendarDate("21112002", true)
    }
}