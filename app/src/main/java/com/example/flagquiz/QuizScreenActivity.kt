package com.example.flagquiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagquiz.ui.theme.FlagQuizTheme
import com.example.flagquiz.model.FlagQuestion

class QuizScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlagQuizTheme {
                Scaffold { innerPadding ->

                    val dummyQuestion = FlagQuestion(
                        id = 1,
                        flagResId = R.drawable.bhutan,
                        options = listOf("Nepal", "India", "China", "Bhutan"),
                        correctAnswer = "Bhutan"
                    )

                    QuizScreen(
                        innerPadding = innerPadding,
                        question = dummyQuestion,
                        onQuitQuiz = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun QuizScreen(
    innerPadding: PaddingValues,
    question: FlagQuestion,
    onQuitQuiz: () -> Unit
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var answerSubmitted by remember { mutableStateOf(false) }
    var isCorrectAnswer by remember { mutableStateOf<Boolean?>(null) }
    var hintUsed by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.flags),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFCC99).copy(alpha = 0.8f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Guess the Country!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Image(
                painter = painterResource(id = question.flagResId),
                contentDescription = "Flag",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(16f / 9f)
                    .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                question.options.forEach { option ->
                    val isOptionSelected = selectedOption == option


                    val buttonColor = when {
                        answerSubmitted && isOptionSelected -> {
                            if (isCorrectAnswer == true) Color.Green.copy(alpha = 0.7f)
                            else Color.Red.copy(alpha = 0.7f)
                        }
                        answerSubmitted && option == question.correctAnswer && !isCorrectAnswer!! -> {
                            Color.Green.copy(alpha = 0.4f)
                        }
                        isOptionSelected -> Color(0xFFF97B57).copy(alpha = 0.7f)
                        else -> Color.White
                    }

                    Button(
                        onClick = {
                            if (!answerSubmitted) {
                                selectedOption = option
                                answerSubmitted = true
                                val correct = (option == question.correctAnswer)
                                isCorrectAnswer = correct

                                if (correct) {
                                    Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Wrong! The answer was ${question.correctAnswer}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColor,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        enabled = !answerSubmitted
                    ) {
                        Text(
                            text = option,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))


            Button(
                onClick = {
                    hintUsed = true
                    Toast.makeText(context, "Hint: Consider the colors and symbols!", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray.copy(alpha = 0.7f)),
                shape = RoundedCornerShape(12.dp),
                enabled = !answerSubmitted && !hintUsed
            ) {
                Text("Hint", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = onQuitQuiz,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Quit Quiz", color = Color.DarkGray, fontSize = 16.sp)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    FlagQuizTheme {
        QuizScreen(
            innerPadding = PaddingValues(0.dp),
            question = FlagQuestion(
                id = 1,
                flagResId = R.drawable.bhutan,
                options = listOf("Nepal", "India", "China", "Bhutan"),
                correctAnswer = "Bhutan"
            ),
            onQuitQuiz = {}
        )
    }
}