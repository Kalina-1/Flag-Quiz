package com.example.flagquiz.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagquiz.R
import com.example.flagquiz.ui.theme.FlagQuizTheme

class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // So UI can go behind system bars, looks modern

        // Get the quiz result data passed from previous screen
        val correctAnswers = intent.getIntExtra("correct_answers", 0)
        val totalQuestions = intent.getIntExtra("total_questions", 0)

        // Set the Compose UI content
        setContent {
            FlagQuizTheme {
                Scaffold { innerPadding ->
                    ResultScreen(
                        innerPadding = innerPadding,
                        correctAnswers = correctAnswers,
                        totalQuestions = totalQuestions,
                        onRestartQuiz = {
                            // Restart quiz by launching QuizScreenActivity again
                            startActivity(Intent(this, QuizScreenActivity::class.java))
                            finish()
                        },
                        onExit = {
                            // Just close this activity and exit results screen
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ResultScreen(
    innerPadding: PaddingValues,
    correctAnswers: Int,
    totalQuestions: Int,
    onRestartQuiz: () -> Unit,
    onExit: () -> Unit
) {
    // Calculate the score percentage out of total questions
    val score = (correctAnswers.toFloat() / totalQuestions.toFloat()) * 100
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        // Background image with flags filling entire screen
        Image(
            painter = painterResource(id = R.drawable.flags),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Semi-transparent overlay container for content readability
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFCC99).copy(alpha = 0.9f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Big title at the top of the results page
            Text(
                text = "Quiz Results",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Circle showing the score with color coding based on performance
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(200.dp)
                    .background(
                        color = when {
                            score >= 80 -> Color(0xFF4CAF50) // Green for excellent
                            score >= 60 -> Color(0xFFFFC107) // Yellow for good
                            else -> Color(0xFFF44336) // Red for poor
                        },
                        shape = RoundedCornerShape(100.dp)
                    )
                    .border(4.dp, Color.Black, RoundedCornerShape(100.dp))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Show percentage big and bold
                    Text(
                        text = "${score.toInt()}%",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    // Show raw correct/total numbers below it
                    Text(
                        text = "$correctAnswers / $totalQuestions",
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Motivational message based on how well the user did
            Text(
                text = when {
                    score >= 80 -> "Excellent! You know your flags well!"
                    score >= 60 -> "Good job! Keep practicing!"
                    else -> "Keep learning! You'll improve!"
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Buttons for retrying quiz or exiting result screen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onRestartQuiz,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Try Again", fontSize = 18.sp)
                }

                Button(
                    onClick = onExit,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Exit", fontSize = 18.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    // Just a preview of the ResultScreen composable with some sample data
    FlagQuizTheme {
        ResultScreen(
            innerPadding = PaddingValues(0.dp),
            correctAnswers = 7,
            totalQuestions = 10,
            onRestartQuiz = {},
            onExit = {}
        )
    }
}
