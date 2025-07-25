package com.example.flagquiz.test

import com.example.flagquiz.viewmodel.QuizViewModel
import com.example.flagquiz.model.Question
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class QuizViewModelTest {

    private lateinit var quizViewModel: QuizViewModel

    @Before
    fun setUp() {
        quizViewModel = QuizViewModel() // Initialize the ViewModel
    }

    @Test
    fun testGetCurrentQuestion() {
        // Get the current question
        val currentQuestion = quizViewModel.getCurrentQuestion()

        // Assert: Verify that the question is not null
        TestCase.assertNotNull("Current question should not be null", currentQuestion)
    }

    @Test
    fun testCheckAnswer_CorrectAnswer() {
        // Get the current question and correct answer
        val currentQuestion = quizViewModel.getCurrentQuestion()
        val correctAnswer = currentQuestion?.correctAnswer

        // Check if the answer is correct
        val isCorrect = quizViewModel.checkAnswer(correctAnswer)

        // Assert: Verify that the answer is correct and score is updated
        TestCase.assertTrue("Answer should be correct", isCorrect)
        TestCase.assertEquals("Score should be 1", 1, quizViewModel.score.value)
    }

    @Test
    fun testCheckAnswer_IncorrectAnswer() {
        // Get the current question and an incorrect answer
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
