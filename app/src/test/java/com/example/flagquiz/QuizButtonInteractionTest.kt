package com.example.flagquiz.test

import com.example.flagquiz.viewmodel.QuizViewModel
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

// This class tests different button interactions and behaviors in the QuizViewModel.
class QuizButtonInteractionTest {

    // Declaring a variable to hold the QuizViewModel instance, which handles quiz logic.
    private lateinit var quizViewModel: QuizViewModel

    // This method is run before each test to set up the environment.
    // It initializes the QuizViewModel, which is the main logic for the quiz.
    @Before
    fun setUp() {
        quizViewModel = QuizViewModel() // Initialize the ViewModel for the quiz screen
    }

    // Test to check if the option buttons get disabled after the user selects an option.
    @Test
    fun testOptionButtonDisabledAfterSelection() {
        // Arrange: Selecting the first option from the list of available options
        val option = quizViewModel.getCurrentQuestion()?.options?.first()

        // Act: Simulate the user selecting the option and checking the answer
        quizViewModel.checkAnswer(option)

        // Assert: Verify that the option button is disabled after selection (the option should not be selectable again)
        TestCase.assertTrue("The option button should be disabled after selection", quizViewModel.isOptionSelected.value)
    }

    // Test to check if the "Next Question" button becomes visible and enabled after selecting an answer.
    @Test
    fun testNextQuestionButtonVisibility() {
        // Arrange: Select an option first to enable the "Next Question" button
        val option = quizViewModel.getCurrentQuestion()?.options?.first()
        quizViewModel.checkAnswer(option)

        // Act: Check visibility or state of "Next Question" button
        val isNextQuestionButtonEnabled = quizViewModel.isOptionSelected.value

        // Assert: Verify that the "Next Question" button is visible and enabled after selection
        TestCase.assertTrue("The 'Next Question' button should be enabled after an option is selected", isNextQuestionButtonEnabled)
    }

    @Test
    fun testScoreUpdatesAfterCorrectAnswer() {
        // Arrange: Get the current question and answer it correctly
        val correctAnswer = quizViewModel.getCurrentQuestion()?.correctAnswer

        // Act: Check if the answer is correct
        quizViewModel.checkAnswer(correctAnswer)

        // Assert: Verify that the score is updated after answering correctly
        TestCase.assertEquals("Score should be 1 after correct answer", 1, quizViewModel.score.value)
    }

    @Test
    fun testTimerResetsForNextQuestion() {
        // Arrange: Set a custom time (e.g., 5 seconds)
        quizViewModel.timeLeft.value = 5

        // Act: Move to the next question
        quizViewModel.nextQuestion()

        // Assert: Verify that the timer resets to 10 seconds for the new question
        TestCase.assertEquals("Timer should reset to 10 seconds after moving to the next question", 10, quizViewModel.timeLeft.value)
    }
}
