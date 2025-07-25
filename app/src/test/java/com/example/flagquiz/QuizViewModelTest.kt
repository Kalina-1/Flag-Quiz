package com.example.flagquiz.test

import com.example.flagquiz.viewmodel.QuizViewModel
import com.example.flagquiz.model.Question
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

// This class contains tests for the QuizViewModel, which handles the logic for the quiz.
class QuizViewModelTest {

    // Declaring a variable to hold the QuizViewModel instance
    private lateinit var quizViewModel: QuizViewModel

    // This method is executed before each test. It initializes the QuizViewModel object.
    @Before
    fun setUp() {
        quizViewModel = QuizViewModel() // Initialize the ViewModel
    }

    // Test to check if the current question is properly retrieved.
    @Test
    fun testGetCurrentQuestion() {
        // Getting the current question from the ViewModel
        val currentQuestion = quizViewModel.getCurrentQuestion()

        // Assert: Verifying that the current question is not null (it should exist)
        TestCase.assertNotNull("Current question should not be null", currentQuestion)
    }

    // Test to check if the correct answer is being processed correctly.
    @Test
    fun testCheckAnswer_CorrectAnswer() {
        // Getting the current question and its correct answer
        val currentQuestion = quizViewModel.getCurrentQuestion()
        val correctAnswer = currentQuestion?.correctAnswer

        // Checking if the answer is correct using the ViewModel's checkAnswer function
        val isCorrect = quizViewModel.checkAnswer(correctAnswer)

        // Assert: Verifying that the answer is correct and the score is updated to 1
        TestCase.assertTrue("Answer should be correct", isCorrect)
        TestCase.assertEquals("Score should be 1", 1, quizViewModel.score.value)
    }

    // Test to check the scenario when the user selects an incorrect answer.
    @Test
    fun testCheckAnswer_IncorrectAnswer() {
        // Getting an incorrect answer (dummy answer in this case)
        val incorrectAnswer = "Incorrect Answer"

        // Check if the answer is correct
        val isCorrect = quizViewModel.checkAnswer(incorrectAnswer)

        // Assert: Verify that the answer is incorrect
        TestCase.assertFalse("Answer should be incorrect", isCorrect)
        TestCase.assertEquals("Score should still be 0", 0, quizViewModel.score.value)
    }

    @Test
    fun testNextQuestion() {
        // Get the current question index using the getter
        val currentQuestionIndex = quizViewModel.getCurrentQuestionIndex()

        // Move to the next question
        quizViewModel.nextQuestion()

        // Assert: Verify the next question index is incremented
        TestCase.assertEquals("Should move to the next question", currentQuestionIndex + 1, quizViewModel.getCurrentQuestionIndex())
    }

    @Test
    fun testResetTimer() {
        // Set a custom time (let's say 5 seconds)
        quizViewModel.timeLeft.value = 5

        // Reset the timer
        quizViewModel.resetTimer()

        // Assert: Verify the timer resets to 10 seconds
        TestCase.assertEquals("Timer should reset to 10 seconds", 10, quizViewModel.timeLeft.value)
    }

    @Test
    fun testIsLastQuestion() {
        // Arrange: Move through all questions until the last one
        val totalQuestions = quizViewModel.getShuffledQuestions().size
        for (i in 0 until totalQuestions - 1) {
            quizViewModel.nextQuestion()
        }

        // Act: Check if it is the last question
        val isLastQuestion = quizViewModel.isLastQuestion()

        // Assert: Verify if the current question is the last one
        TestCase.assertTrue("It should be the last question", isLastQuestion)
    }

    @Test
    fun testResetHint() {
        // Arrange: Set the hint to true
        quizViewModel.showHint.value = true

        // Act: Reset the hint
        quizViewModel.resetHint()

        // Assert: Verify the hint is reset to false
        TestCase.assertFalse("Hint should be reset to false", quizViewModel.showHint.value)
    }
}
