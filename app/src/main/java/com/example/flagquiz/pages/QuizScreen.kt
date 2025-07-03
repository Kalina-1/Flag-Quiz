package com.example.flagquiz.pages

import android.os.CountDownTimer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.flagquiz.viewmodel.QuizViewModel
import com.example.flagquiz.ui.theme.FlagQuizTheme
import com.example.flagquiz.R

@Composable
fun QuizScreen() {
    // Access ViewModel
    val quizViewModel: QuizViewModel = viewModel()

    // Get the current question from the ViewModel
    val currentQuestion = quizViewModel.getCurrentQuestion()
    val timeLeft = quizViewModel.timeLeft.value
    val isOptionSelected = quizViewModel.isOptionSelected.value
    // val selectedOption = quizViewModel.selectedOption.value // Removed: Variable 'selectedOption' is never used
    // val progress = quizViewModel.progress.value // Removed: Variable 'progress' is never used

    // State to manage feedback visibility
    val showFeedback = remember { mutableStateOf(false) }
    val feedbackMessage = remember { mutableStateOf("") }
    val feedbackColor = remember { mutableStateOf(Color.Red) }

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
                if (!isOptionSelected) {
                    quizViewModel.nextQuestion()  // Automatically move to the next question after time finishes
                }
            }
        }
    }

    // Start the timer when the screen is loaded or when the question changes
    LaunchedEffect(currentQuestion) {
        quizViewModel.resetTimer()
        timer.start()
    }

    // Show feedback for 2 seconds before moving to next question
    LaunchedEffect(isOptionSelected) {
        if (isOptionSelected) {
            // Delay for 2 seconds before moving to the next question
            kotlinx.coroutines.delay(2000) // 2 seconds delay
            quizViewModel.nextQuestion()  // Move to the next question after 2 seconds
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
        // Display the timer
        Text(
            text = "$timeLeft",
            fontSize = 40.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // Progress Bar (timer visualized)
        LinearProgressIndicator(
            progress = { quizViewModel.progress.value }, // Corrected: Use lambda for progress
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .padding(bottom = 20.dp),
            color = Color.Red,
            trackColor = Color.Gray
        )

        // Display Flag Image
        currentQuestion?.let { question ->
            Image(
                painter = painterResource(id = question.flagImage),
                contentDescription = "Flag",
                modifier = Modifier
                    .size(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )

            // Display Options (Multiple Choice)
            question.options.forEach { option ->
                Button(
                    onClick = {
                        if (!isOptionSelected) {
                            quizViewModel.selectedOption.value = option
                            quizViewModel.isOptionSelected.value = true
                            showFeedback.value = true // Show feedback after selection

                            // Check if the selected option is correct or incorrect
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
                    enabled = !isOptionSelected,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White // Option buttons color set to white
                    )
                ) {
                    Text(text = option, color = Color.Black) // Text color for option buttons set to black
                }
            }
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

        // Display Finish Button if it's the last question
        if (quizViewModel.isLastQuestion()) {
            Button(
                onClick = {
                    // End the quiz here or navigate to a results screen
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57)) // Finish button color
            ) {
                Text("Finish", color = Color.White)
            }
        } else if (isOptionSelected) {
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

@Preview(showBackground = true)
@Composable
fun PreviewQuizScreen() {
    FlagQuizTheme {
        QuizScreen()
    }
}