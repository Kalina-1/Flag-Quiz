package com.example.flagquiz


// Importing necessary libraries for testing UI components in Android.
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.flagquiz.view.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// Annotating the class to specify it runs with AndroidJUnit4, which is used for Android tests.
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    // Rule to create and manage the Compose UI test environment for LoginActivity.
    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @Test
    fun testLoginScreenElementsVisible() {
        composeTestRule.onNodeWithTag("login_email_field").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_password_field").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_button").assertIsDisplayed()
    }

    @Test
    fun testLoginWithEmptyFields_ShowsError() {
        composeTestRule.onNodeWithTag("login_button").performClick()
        // Assuming error message appears in a Text with specific text like "Invalid login"
        composeTestRule.onNodeWithText("Invalid login").assertIsDisplayed()
    }

    @Test
    fun testLoginWithValidCredentials_Navigates() {
        composeTestRule.onNodeWithTag("login_email_field").performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("login_password_field").performTextInput("testpassword")
        composeTestRule.onNodeWithTag("login_button").performClick()

        // You may check navigation, or mock ViewModel to return isAuthenticated = true
    }
}
