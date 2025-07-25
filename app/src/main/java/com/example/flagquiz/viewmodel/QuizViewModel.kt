
package com.example.flagquiz.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.flagquiz.model.Question
import com.example.flagquiz.R
import kotlin.random.Random

class QuizViewModel : ViewModel() {
    // Pool of 30 questions
    private val allQuestions = listOf<Question>(
        Question(
            flagImage = R.drawable.nepal,
            options = listOf("Spain", "Canada", "Nepal", "India"),
            correctAnswer = "Nepal",
            capitalHint = "Kathmandu"
        ),
        Question(
            flagImage = R.drawable.usa,
            options = listOf("USA", "Mexico", "Brazil", "Argentina"),
            correctAnswer = "USA",
            capitalHint = "Washington D.C."
        ),
        Question(
            flagImage = R.drawable.uk,
            options = listOf("United Kingdom", "France", "Italy", "Germany"),
            correctAnswer = "United Kingdom",
            capitalHint = "London"
        ),
        Question(
            flagImage = R.drawable.japan,
            options = listOf("China", "Japan", "Korea", "Vietnam"),
            correctAnswer = "Japan",
            capitalHint = "Tokyo"
        ),
        Question(
            flagImage = R.drawable.italy,
            options = listOf("Italy", "Germany", "France", "Spain"),
            correctAnswer = "Italy",
            capitalHint = "Rome"
        ),
        Question(
            flagImage = R.drawable.germany,
            options = listOf("Italy", "Germany", "Austria", "Switzerland"),
            correctAnswer = "Germany",
            capitalHint = "Berlin"
        ),
        Question(
            flagImage = R.drawable.france,
            options = listOf("France", "Belgium", "Netherlands", "Luxembourg"),
            correctAnswer = "France",
            capitalHint = "Paris"
        ),
        Question(
            flagImage = R.drawable.canada,
            options = listOf("Canada", "USA", "Mexico", "Cuba"),
            correctAnswer = "Canada",
            capitalHint = "Ottawa"
        ),
        Question(
            flagImage = R.drawable.mexico,
            options = listOf("USA", "Mexico", "Colombia", "Argentina"),
            correctAnswer = "Mexico",
            capitalHint = "Mexico City"
        ),
        Question(
            flagImage = R.drawable.spain,
            options = listOf("Spain", "Italy", "Portugal", "France"),
            correctAnswer = "Spain",
            capitalHint = "Madrid"
        ),
        Question(
            flagImage = R.drawable.india,
            options = listOf("Pakistan", "India", "Nepal", "Bangladesh"),
            correctAnswer = "India",
            capitalHint = "New Delhi"
        ),
        Question(
            flagImage = R.drawable.china,
            options = listOf("China", "Japan", "Korea", "Taiwan"),
            correctAnswer = "China",
            capitalHint = "Beijing"
        ),
        Question(
            flagImage = R.drawable.brazil,
            options = listOf("Brazil", "Argentina", "Chile", "Peru"),
            correctAnswer = "Brazil",
            capitalHint = "Brasília"
        ),
        Question(
            flagImage = R.drawable.australia,
            options = listOf("Australia", "New Zealand", "Fiji", "Papua New Guinea"),
            correctAnswer = "Australia",
            capitalHint = "Canberra"
        ),
        Question(
            flagImage = R.drawable.south_korea,
            options = listOf("North Korea", "South Korea", "Japan", "China"),
            correctAnswer = "South Korea",
            capitalHint = "Seoul"
        ),
        Question(
            flagImage = R.drawable.saudi_arabia,
            options = listOf("Saudi Arabia", "Iraq", "Iran", "Egypt"),
            correctAnswer = "Saudi Arabia",
            capitalHint = "Riyadh"
        ),
        Question(
            flagImage = R.drawable.south_africa,
            options = listOf("South Africa", "Zimbabwe", "Kenya", "Nigeria"),
            correctAnswer = "South Africa",
            capitalHint = "Pretoria"
        ),
        Question(
            flagImage = R.drawable.argentina,
            options = listOf("Argentina", "Brazil", "Chile", "Peru"),
            correctAnswer = "Argentina",
            capitalHint = "Buenos Aires"
        ),
        Question(
            flagImage = R.drawable.colombia,
            options = listOf("Colombia", "Venezuela", "Ecuador", "Peru"),
            correctAnswer = "Colombia",
            capitalHint = "Bogotá"
        ),
        Question(
            flagImage = R.drawable.egypt,
            options = listOf("Egypt", "Sudan", "Ethiopia", "Libya"),
            correctAnswer = "Egypt",
            capitalHint = "Cairo"
        ),
        Question(
            flagImage = R.drawable.kenya,
            options = listOf("Kenya", "Tanzania", "Uganda", "Rwanda"),
            correctAnswer = "Kenya",
            capitalHint = "Nairobi"
        ),
        Question(
            flagImage = R.drawable.indonesia,
            options = listOf("Indonesia", "Malaysia", "Singapore", "Brunei"),
            correctAnswer = "Indonesia",
            capitalHint = "Jakarta"
        ),
        Question(
            flagImage = R.drawable.turkey,
            options = listOf("Turkey", "Greece", "Cyprus", "Syria"),
            correctAnswer = "Turkey",
            capitalHint = "Ankara"
        ),
        Question(
            flagImage = R.drawable.malaysia,
            options = listOf("Malaysia", "Singapore", "Thailand", "Indonesia"),
            correctAnswer = "Malaysia",
            capitalHint = "Kuala Lumpur"
        ),
        Question(
            flagImage = R.drawable.poland,
            options = listOf("Poland", "Czech Republic", "Slovakia", "Hungary"),
            correctAnswer = "Poland",
            capitalHint = "Warsaw"
        ),
        Question(
            flagImage = R.drawable.norway,
            options = listOf("Norway", "Sweden", "Finland", "Denmark"),
            correctAnswer = "Norway",
            capitalHint = "Oslo"
        ),
        Question(
            flagImage = R.drawable.sweden,
            options = listOf("Sweden", "Norway", "Finland", "Denmark"),
            correctAnswer = "Sweden",
            capitalHint = "Stockholm"
        ),
        Question(
            flagImage = R.drawable.finland,
            options = listOf("Finland", "Sweden", "Norway", "Estonia"),
            correctAnswer = "Finland",
            capitalHint = "Helsinki"
        ),
        Question(
            flagImage = R.drawable.switzerland,
            options = listOf("Switzerland", "Austria", "Germany", "France"),
            correctAnswer = "Switzerland",
            capitalHint = "Bern"
        ),
        Question(
            flagImage = R.drawable.nigeria,
            options = listOf("Nigeria", "Ghana", "Kenya", "Uganda"),
            correctAnswer = "Nigeria",
            capitalHint = "Abuja"
        ),
        Question(
            flagImage = R.drawable.iraq,
            options = listOf("Iraq", "Iran", "Syria", "Jordan"),
            correctAnswer = "Iraq",
            capitalHint = "Baghdad"
        ),
        Question(
            flagImage = R.drawable.venezuela,
            options = listOf("Venezuela", "Colombia", "Peru", "Brazil"),
            correctAnswer = "Venezuela",
            capitalHint = "Caracas"
        )
    )

    // Randomly select 10 questions from the pool
    private val selectedQuestions = allQuestions.shuffled().take(10)

    // Shuffle the options for each question
    private val shuffledQuestions = selectedQuestions.map { it.copy(options = it.options.shuffled()) }

    private var currentQuestionIndex = 0
    val score = mutableStateOf(0)
    val timeLeft = mutableStateOf(10)
    val isOptionSelected = mutableStateOf(false) // Track if an option is selected
    val selectedOption = mutableStateOf<String?>(null)
    val progress = mutableStateOf(0f)
    val showHint = mutableStateOf(false)

    // Return the current question
    fun getCurrentQuestion(): Question? {
        return if (currentQuestionIndex < shuffledQuestions.size) shuffledQuestions[currentQuestionIndex] else null
    }

    // Check answer
    fun checkAnswer(selected: String?): Boolean {
        val isCorrect = selected == getCurrentQuestion()?.correctAnswer
        if (isCorrect) {
            score.value += 1 // Increment score if the answer is correct
        }
        // Mark that an option has been selected
        isOptionSelected.value = true
        selectedOption.value = selected
        return isCorrect
    }

    // Move to the next question
    fun nextQuestion() {
        if (currentQuestionIndex < shuffledQuestions.size - 1) {
            currentQuestionIndex++
            // Reset states for the next question
            isOptionSelected.value = false
            selectedOption.value = null
            timeLeft.value = 10  // Reset the timer to 10 seconds
            progress.value = 0f
        }
    }

    // Reset the timer state
    fun resetTimer() {
        timeLeft.value = 10
        progress.value = 0f
    }

    // Check if it's the last question
    fun isLastQuestion(): Boolean {
        return currentQuestionIndex == shuffledQuestions.size - 1
    }

    // **Getter methods for testing:**

    // Get current question index
    fun getCurrentQuestionIndex(): Int {
        return currentQuestionIndex
    }

    // Get shuffled questions (useful for testing)
    fun getShuffledQuestions(): List<Question> {
        return shuffledQuestions
    }

    // Reset hint value
    fun resetHint() {
        showHint.value = false
    }
}
