package com.example.flagquiz.test

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.flagquiz.view.LoginActivity
import com.example.flagquiz.view.LoginBody
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.junit.Rule
import org.junit.Test

class LoginActivityInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @Test
    fun testLoginWithValidCredentials() {
        // Set content for the test using ComposeTestRule
        composeTestRule.setContent {
            LoginBody(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance())
        }

        // Input email
        composeTestRule.onNodeWithTag("email")
            .performTextInput("manisha123@gmail.com")

        // Input password
        composeTestRule.onNodeWithTag("password")
            .performTextInput("manisha123")

        // Ensure the login button is visible and then click it
        composeTestRule.onNodeWithTag("loginButton")
            .assertIsDisplayed()
            .performClick()

        // Wait for UI to idle and ensure navigation
        composeTestRule.waitForIdle()

        // Optional: You can add a small delay to give enough time for the next screen to load
        composeTestRule.mainClock.advanceTimeBy(3000) // Wait for 3 seconds to allow the screen to load

        // Verify if the user navigates to NavigationActivity
        // Look for a text that exists in the NavigationActivity screen
        composeTestRule.onNodeWithText("Home") // "Home" should exist in the NavigationActivity UI
            .assertIsDisplayed()

        // Optional: You can also verify more elements that should exist in NavigationActivity
        composeTestRule.onNodeWithText("Flag Quiz App") // Check for other UI elements in NavigationActivity
            .assertIsDisplayed()
    }
}
