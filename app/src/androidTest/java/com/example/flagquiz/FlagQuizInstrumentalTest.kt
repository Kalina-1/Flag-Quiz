package com.example.flagquiz.test

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.flagquiz.view.QuizScreenActivity
import org.junit.Rule
import org.junit.Test

class FlagQuizInstrumentalTest {

    // The Compose Test Rule is automatically set for each test, so no need to call setContent again in each test
    @get:Rule
    val composeTestRule = createAndroidComposeRule<QuizScreenActivity>()

    @Test
    fun testOptionButtonClick_DisablesButtonAfterSelection() {
        // The setContent call is already handled by the Compose Test Rule, so you don't need to call it again

        // Click on the "Nepal" option
        composeTestRule.onNodeWithText("Nepal")
            .assertIsDisplayed()  // Ensure the option is displayed
            .performClick()  // Click the option

        // Check that the "Nepal" option button is now disabled
        composeTestRule.onNodeWithText("Nepal")
            .assertIsNotEnabled()  // Assert the button is disabled after selection
    }

    @Test
    fun testNextQuestionButtonVisibility() {
        // No need to call setContent again, ComposeTestRule handles it
        // Interact with the option (for example, click the option)
        composeTestRule.onNodeWithText("Nepal")
            .assertIsDisplayed()  // Ensure it's displayed
            .performClick()  // Click the option

        // Assert that the 'Next Question' button is displayed after selecting an option
        composeTestRule.onNodeWithText("Next Question")
            .assertIsDisplayed()  // Ensure the Next Question button is visible after option selection
    }
}
