package com.example.flagquiz.test

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.flagquiz.view.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @Test
    fun testLoginWithValidCredentials() {
        // Set content for the test using ComposeTestRule
        composeTestRule.setContent {
            // Set the content once for the test
            // The LoginViewModel should be already initialized in your LoginActivity
            // You may mock the login or the Firebase functionality to control the flow
        }

        // Enter email
        composeTestRule.onNodeWithTag("email")
            .performTextInput("zoya@gmail.com")

        // Enter password
        composeTestRule.onNodeWithTag("password")
            .performTextInput("zoya123")

        // Click the Login button
        composeTestRule.onNodeWithTag("loginButton")
            .performClick()

        // Wait for UI to idle
        composeTestRule.waitForIdle()

        // Verify successful login
        composeTestRule.onNodeWithText("Home") // Ensure the next screen is displayed
            .assertIsDisplayed()

        // You can add more verifications if required
        composeTestRule.onNodeWithText("Flag Quiz App") // Check other elements in NavigationActivity
            .assertIsDisplayed()
    }

    @Test
    fun testEmptyEmail_ShowsNoNavigation() {
        // Attempt to login with an empty email
        composeTestRule.onNodeWithTag("email")
            .performTextInput("") // No email

        // Enter password
        composeTestRule.onNodeWithTag("password")
            .performTextInput("zoya123")

        // Click the Login button
        composeTestRule.onNodeWithTag("loginButton")
            .performClick()

        // Wait for UI to idle
        composeTestRule.waitForIdle()

        // Ensure no navigation occurs and we're still on the login screen
        composeTestRule.onNodeWithText("Log In") // This text should stay on the login screen
            .assertIsDisplayed()
    }
}
