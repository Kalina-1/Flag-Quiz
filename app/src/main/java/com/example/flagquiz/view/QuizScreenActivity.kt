package com.example.flagquiz.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.flagquiz.R // Make sure this import is correct for your R file
import com.example.flagquiz.model.Question // Import your Question data class
import com.example.flagquiz.ui.theme.FlagQuizTheme

class QuizScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlagQuizTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Initialize your list of questions here
                    val allQuestions = remember {
                        listOf(
                            Question(
                                flagImage = R.drawable.bhutan, // Ensure you have this drawable
                                options = listOf("Bhutan", "Nepal", "India", "China"),
                                correctAnswer = "Bhutan",
                                capitalHint = "Thimphu"
                            ),
                            Question(
                                flagImage = R.drawable.nepal, // Ensure you have this drawable
                                options = listOf("Nepal", "Pakistan", "Bangladesh", "Sri Lanka"),
                                correctAnswer = "Nepal",
                                capitalHint = "Kathmandu"
                            ),
                            Question(
                                flagImage = R.drawable.india, // Ensure you have this drawable
                                options = listOf("India", "Myanmar", "Thailand", "Vietnam"),
                                correctAnswer = "India",
                                capitalHint = "New Delhi"
                            ),
                            Question(
                                flagImage = R.drawable.iraq, // Ensure you have this drawable
                                options = listOf("Pakistan", "Afghanistan", "Iraq", "Uzbekistan"),
                                correctAnswer = "Pakistan",
                                capitalHint = "Islamabad"
                            ),
                            Question(
                                flagImage = R.drawable.malaysia, // Ensure you have this drawable
                                options = listOf("Bangladesh", "Indonesia", "Malaysia", "Philippines"),
                                correctAnswer = "Bangladesh",
                                capitalHint = "Dhaka"
                            )
                            // Add more questions as needed
                        )
                    }

                    // Call the main quiz screen composable
                    QuizScreen(
                        questions = allQuestions,
                        paddingValues = innerPadding
                    )
                }
            }
        }
    }
}

@Composable
fun QuizScreen(
    questions: List<Question>,
    paddingValues: PaddingValues // Padding provided by the Scaffold
) {
    // State variables for managing quiz progress
    var currentQuestionIndex by rememberSaveable { mutableIntStateOf(0) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var selectedOption by rememberSaveable { mutableStateOf<String?>(null) }
    var showFeedback by rememberSaveable { mutableStateOf(false) } // To show correct/incorrect feedback

    // Get the current question or null if quiz is finished
    val currentQuestion = if (currentQuestionIndex < questions.size) {
        questions[currentQuestionIndex]
    } else {
        null // Quiz completed
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) // Apply padding from Scaffold
            .padding(16.dp), // Additional padding for content within the screen
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (currentQuestion != null) {
            // --- Display Current Question UI ---
            Text(
                text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Image(
                painter = painterResource(id = currentQuestion.flagImage),
                contentDescription = "Flag of current country",
                modifier = Modifier
                    .size(200.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Options as clickable buttons
            currentQuestion.options.forEach { option ->
                Button(
                    onClick = {
                        if (!showFeedback) { // Only allow selection if not showing feedback
                            selectedOption = option
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    // Change button colors based on selection and feedback state
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            showFeedback && option == currentQuestion.correctAnswer -> Color.Green // Correct answer
                            showFeedback && option == selectedOption && option != currentQuestion.correctAnswer -> Color.Red // Incorrect selected
                            selectedOption == option && !showFeedback -> MaterialTheme.colorScheme.primary // Currently selected
                            else -> MaterialTheme.colorScheme.surfaceVariant // Default
                        },
                        contentColor = when {
                            showFeedback && (option == currentQuestion.correctAnswer || (option == selectedOption && option != currentQuestion.correctAnswer)) -> Color.White
                            selectedOption == option && !showFeedback -> MaterialTheme.colorScheme.onPrimary
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    ),
                    enabled = !showFeedback // Disable buttons if feedback is shown
                ) {
                    Text(option)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Submit/Next Button
            if (!showFeedback) {
                // Submit button if feedback is not yet shown
                Button(
                    onClick = {
                        if (selectedOption != null) {
                            if (selectedOption == currentQuestion.correctAnswer) {
                                score++
                            }
                            showFeedback = true // Show feedback after submission
                        }
                    },
                    enabled = selectedOption != null // Enable only if an option is selected
                ) {
                    Text("Submit Answer")
                }
            } else {
                // Next Question button if feedback is shown
                Button(
                    onClick = {
                        selectedOption = null // Reset selection
                        showFeedback = false // Hide feedback
                        currentQuestionIndex++ // Move to the next question
                    }
                ) {
                    Text("Next Question")
                }
            }

            // Feedback Text (Correct/Incorrect & Hint)
            if (showFeedback) {
                Spacer(modifier = Modifier.height(16.dp))
                val feedbackMessage = if (selectedOption == currentQuestion.correctAnswer) {
                    "Correct!"
                } else {
                    "Incorrect. The correct answer was ${currentQuestion.correctAnswer}."
                }
                Text(
                    text = feedbackMessage,
                    color = if (selectedOption == currentQuestion.correctAnswer) Color.Green else Color.Red,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Hint: The capital is ${currentQuestion.capitalHint}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

        } else {
            // --- Quiz Finished UI ---
            QuizResultScreen(score = score, totalQuestions = questions.size) {
                // On play again, reset quiz state
                currentQuestionIndex = 0
                score = 0
                selectedOption = null
                showFeedback = false
            }
        }
    }
}

@Composable
fun QuizResultScreen(score: Int, totalQuestions: Int, onPlayAgain: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Quiz Finished!", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Your Score: $score / $totalQuestions", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onPlayAgain) {
            Text("Play Again")
        }
    }
}