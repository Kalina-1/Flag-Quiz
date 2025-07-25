package com.example.flagquiz.test

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.flagquiz.view.LoginActivity
import com.example.flagquiz.viewmodel.LoginViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.flagquiz.view.LoginBody
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import kotlin.jvm.java

@RunWith(AndroidJUnit4::class)
class LoginActivityInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    private val mockFirebaseAuth: FirebaseAuth = mock(FirebaseAuth::class.java)
    private val mockFirebaseDatabase: FirebaseDatabase = mock(FirebaseDatabase::class.java)

    @Test
    fun testLoginWithValidCredentials() {
        // Set content for the test using ComposeTestRule
        composeTestRule.setContent {
            val loginViewModel: LoginViewModel = LoginViewModel().apply {
                // Mock Firebase Auth call
                whenever(mockFirebaseAuth.signInWithEmailAndPassword(any(), any()))
                    .thenReturn(Task.forResult(mock(AuthResult::class.java))) // Mock successful login
            }

            LoginBody(viewModel = loginViewModel)
        }

        // Enter email
        composeTestRule.onNodeWithTag("email")
            .performTextInput("manisha123@gmail.com")

        // Enter password
        composeTestRule.onNodeWithTag("password")
            .performTextInput("manisha123")

        // Click Login
        composeTestRule.onNodeWithTag("loginButton")
            .performClick()

        // Wait for UI to idle
        composeTestRule.waitForIdle()

        // Verify successful login
        composeTestRule.onNodeWithText("Home") // Check that the next screen is displayed
            .assertIsDisplayed()
    }
}
