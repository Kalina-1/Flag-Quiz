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

// I renamed this activity from QuizActivity to QuizScreenActivity to match it with my AndroidManifest
class QuizScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Wrapped it in the app's theme for consistent look
            FlagQuizTheme {
                QuizScreen(onFinishClicked = {
                    // When the user clicks finish, just close this screen
                    finish()
                })
            }
        }
    }
}

@Composable
fun QuizScreen(onFinishClicked: () -> Unit) {
    val quizViewModel: QuizViewModel = viewModel()

    // I pull all necessary states from the ViewModel
    val currentQuestion = quizViewModel.getCurrentQuestion()
    val timeLeft = quizViewModel.timeLeft.value
    val isOptionSelected = quizViewModel.isOptionSelected.value
    val progress = quizViewModel.progress.value
    val showHint = quizViewModel.showHint.value

    // These help show correct/incorrect message
    val showFeedback = remember { mutableStateOf(false) }
    val feedbackMessage = remember { mutableStateOf("") }
    val feedbackColor = remember { mutableStateOf(Color.Red) }

    // Flag to check when quiz is done
    val isQuizFinished = remember { mutableStateOf(false) }

    // Used CountDownTimer to auto-move to next question every 10 seconds
    val timer = remember {
        object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                quizViewModel.timeLeft.value = (millisUntilFinished / 1000).toInt()
                quizViewModel.progress.value = millisUntilFinished / 10000f
            }

            override fun onFinish() {
                quizViewModel.timeLeft.value = 0
                quizViewModel.progress.value = 0f
                // If user didn't select any option, move to next question
                if (!isOptionSelected) {
                    quizViewModel.nextQuestion()
                }
            }
        }
    }

    // Restart the timer and reset feedback every time a new question loads
    LaunchedEffect(currentQuestion) {
        quizViewModel.resetTimer()
        quizViewModel.showHint.value = false
        timer.start()
        showFeedback.value = false
        feedbackMessage.value = ""
        feedbackColor.value = Color.Red
    }

    // Wait a bit before moving to next question after option is selected
    LaunchedEffect(isOptionSelected) {
        if (isOptionSelected) {
            timer.cancel()
            kotlinx.coroutines.delay(2000) // Wait to show feedback
            showFeedback.value = false
            if (quizViewModel.isLastQuestion()) {
                isQuizFinished.value = true
            }
        }
    }

    // Main UI of quiz
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFCC99).copy(alpha = 0.8f)) // Added soft background color
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isQuizFinished.value) {
            // When quiz ends, show score and finish button
            Text(
                text = "Your Score: ${quizViewModel.score.value}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Green,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Button(
                onClick = { onFinishClicked() },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57))
            ) {
                Text("Finish Quiz", color = Color.White)
            }
        } else {
            // Timer countdown display
            Text(
                text = "$timeLeft",
                fontSize = 40.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Progress bar
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .padding(bottom = 20.dp),
                color = Color.Red,
                trackColor = Color.Gray
            )

            currentQuestion?.let { question ->
                // Show flag image for the question
                Image(
                    painter = painterResource(id = question.flagImage),
                    contentDescription = "Flag",
                    modifier = Modifier
                        .size(150.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )

                // Render all option buttons
                question.options.forEach { option ->
                    Button(
                        onClick = {
                            if (!isOptionSelected) {
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
                        enabled = !isOptionSelected,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(text = option, color = Color.Black)
                    }
                }
            }

            // Show hint button if not shown already
            if (!isOptionSelected && !showHint) {
                Button(
                    onClick = {
                        quizViewModel.showHint.value = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57))
                ) {
                    Text("Show Hint: Capital City", color = Color.White)
                }
            }

            // Show the capital city as a hint
            if (showHint && currentQuestion != null) {
                Text(
                    text = "Capital: ${currentQuestion.capitalHint}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }

            // Show feedback for selected option
            if (showFeedback.value) {
                Text(
                    text = feedbackMessage.value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = feedbackColor.value,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }

            // Show either Finish or Next Question button after answering
            if (isOptionSelected) {
                if (quizViewModel.isLastQuestion()) {
                    Button(
                        onClick = { onFinishClicked() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57))
                    ) {
                        Text("Finish", color = Color.White)
                    }
                } else {
                    Button(
                        onClick = { quizViewModel.nextQuestion() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57))
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
    FlagQuizTheme {
        QuizScreen(onFinishClicked = { /* Nothing here for preview */ })
    }
}