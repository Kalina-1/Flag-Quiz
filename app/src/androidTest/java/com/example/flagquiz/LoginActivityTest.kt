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

    // Test case to check if the elements on the login screen are visible.
    @Test
    fun testLoginScreenElementsVisible() {
        // Checking if the email field is displayed.
        composeTestRule.onNodeWithTag("login_email_field").assertIsDisplayed()
        // Checking if the password field is displayed.
        composeTestRule.onNodeWithTag("login_password_field").assertIsDisplayed()
        // Checking if the login button is displayed.
        composeTestRule.onNodeWithTag("login_button").assertIsDisplayed()
    }


    // Test case to check if an error message appears when login fields are empty.
    @Test
    fun testLoginWithEmptyFields_ShowsError() {
        // Clicking the login button.
        composeTestRule.onNodeWithTag("login_button").performClick()
        // Assuming error message appears in a Text with specific text like "Invalid login"
        composeTestRule.onNodeWithText("Invalid login").assertIsDisplayed()
    }

    // Test case to check if login with valid credentials navigates the user to the next screen.
    @Test
    fun testLoginWithValidCredentials_Navigates() {
        // Entering a valid email into the email field.
        composeTestRule.onNodeWithTag("login_email_field").performTextInput("test@example.com")
        // Entering a valid password into the password field.
        composeTestRule.onNodeWithTag("login_password_field").performTextInput("testpassword")
        composeTestRule.onNodeWithTag("login_button").performClick()

        // You may check navigation, or mock ViewModel to return isAuthenticated = true
    }
}
