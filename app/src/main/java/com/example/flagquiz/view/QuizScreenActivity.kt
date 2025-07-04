package com.example.flagquiz.view

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flagquiz.R
import com.example.flagquiz.ui.theme.FlagQuizTheme
import com.example.flagquiz.viewmodel.QuizViewModel

// RENAMED CLASS: Changed from QuizActivity to QuizScreenActivity to match manifest
class QuizScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // It's good practice to wrap your top-level composable in your app's theme
            FlagQuizTheme {
                QuizScreen(onFinishClicked = {
                    // Finish the activity when the Finish button is clicked
                    finish()
                })
            }
        }
    }
}

@Composable
fun QuizScreen(onFinishClicked: () -> Unit) {
    val quizViewModel: QuizViewModel = viewModel()

    // Get the current question from the ViewModel
    val currentQuestion = quizViewModel.getCurrentQuestion()
    val timeLeft = quizViewModel.timeLeft.value
    val isOptionSelected = quizViewModel.isOptionSelected.value
    val progress = quizViewModel.progress.value
    val showHint = quizViewModel.showHint.value // Track if hint is shown

    // State to manage feedback visibility
    val showFeedback = remember { mutableStateOf(false) }
    val feedbackMessage = remember { mutableStateOf("") }
    val feedbackColor = remember { mutableStateOf(Color.Red) }

    // Track if the quiz is finished
    val isQuizFinished = remember { mutableStateOf(false) }

    // Initialize the countdown timer (10 seconds per question)
    val timer = remember {
        object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                quizViewModel.timeLeft.value = (millisUntilFinished / 1000).toInt()
                quizViewModel.progress.value = millisUntilFinished / 10000f
            }

            override fun onFinish() {
                quizViewModel.timeLeft.value = 0
                quizViewModel.progress.value = 0f
                if (!isOptionSelected) { // Only move to next if no option was selected
                    quizViewModel.nextQuestion()
                }
            }
        }
    }

    // Start the timer when the screen is loaded or when the question changes
    LaunchedEffect(currentQuestion) {
        quizViewModel.resetTimer()
        quizViewModel.showHint.value = false // Reset the hint when moving to a new question
        timer.start()
        showFeedback.value = false // Also reset feedback on new question
        feedbackMessage.value = ""
        feedbackColor.value = Color.Red
    }

    // Handle option selection feedback and next question logic
    LaunchedEffect(isOptionSelected) {
        if (isOptionSelected) {
            timer.cancel() // Stop the timer once an option is selected
            kotlinx.coroutines.delay(2000) // Delay for 2 seconds for feedback
            showFeedback.value = false // Hide feedback after delay
            if (quizViewModel.isLastQuestion()) {
                isQuizFinished.value = true // Mark quiz as finished
            } else {
                // Next question logic is now primarily in LaunchedEffect(currentQuestion)
                // This ensures timer reset for the new question.
            }
        }
    }


    // Layout for displaying the quiz question with updated background color
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFCC99).copy(alpha = 0.8f)) // Set the background color with alpha
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isQuizFinished.value) {
            // Display final score when the quiz is finished
            Text(
                text = "Your Score: ${quizViewModel.score.value}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Green,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Finish button to navigate back to the NavigationActivity
            Button(
                onClick = {
                    onFinishClicked() // Invoke the finish action passed from the parent activity
                },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57)) // Finish button color
            ) {
                Text("Finish Quiz", color = Color.White)
            }
        } else {
            // Display timer, flag, options, and other quiz elements

            Text(
                text = "$timeLeft",
                fontSize = 40.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            LinearProgressIndicator(
                progress = quizViewModel.progress.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .padding(bottom = 20.dp),
                color = Color.Red,
                trackColor = Color.Gray
            )

            currentQuestion?.let { question ->
                Image(
                    painter = painterResource(id = question.flagImage),
                    contentDescription = "Flag",
                    modifier = Modifier
                        .size(150.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )

                question.options.forEach { option ->
                    Button(
                        onClick = {
                            if (!isOptionSelected) { // Only allow selection if an option hasn't been chosen yet
                                quizViewModel.selectedOption.value = option
                                quizViewModel.isOptionSelected.value = true
                                showFeedback.value = true
                                if (quizViewModel.checkAnswer(option)) {
                                    feedbackMessage.value = "Correct!"
                                    feedbackColor.value = Color.Green
                                } else {
                                    feedbackMessage.value = "Incorrect"
                                    feedbackColor.value = Color.Red
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        enabled = !isOptionSelected, // Disable buttons once an option is selected
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White // Option buttons color set to white
                        )
                    ) {
                        Text(text = option, color = Color.Black)
                    }
                }
            }

            // Display the Hint Button only when no option is selected and hint isn't already shown
            if (!isOptionSelected && !showHint) {
                Button(
                    onClick = {
                        quizViewModel.showHint.value = true // Show the hint when clicked
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    // enabled = !isOptionSelected, // This is redundant due to the 'if' condition
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF97B57) // Hint button color
                    )
                ) {
                    Text("Show Hint: Capital City", color = Color.White)
                }
            }

            // Display the capital city hint if it's available
            if (showHint && currentQuestion != null) {
                Text(
                    text = "Capital: ${currentQuestion.capitalHint}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }

            // Display Feedback (Correct or Incorrect)
            if (showFeedback.value) {
                Text(
                    text = feedbackMessage.value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = feedbackColor.value,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }

            // Conditionally display "Finish Quiz" or "Next Question" button
            // This logic is slightly adjusted to ensure 'Next Question' button appears only after an option is selected.
            // The isQuizFinished.value check handles the final state.
            if (isOptionSelected) { // Only show next/finish button if an option has been selected
                if (quizViewModel.isLastQuestion()) {
                    Button(
                        onClick = {
                            onFinishClicked() // Invoke the finish action passed from the parent activity
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57)) // Finish button color
                    ) {
                        Text("Finish", color = Color.White)
                    }
                } else {
                    // This button should ideally trigger the next question after feedback
                    // The LaunchedEffect(isOptionSelected) already handles automatic next question after delay.
                    // This button might be for manual advancement if user doesn't want to wait.
                    Button(
                        onClick = { quizViewModel.nextQuestion() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57)) // Next Question button color
                    ) {
                        Text("Next Question", color = Color.White)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizScreen() {
    FlagQuizTheme  {
        QuizScreen(onFinishClicked = { /* Do nothing for preview */ })
    }
}