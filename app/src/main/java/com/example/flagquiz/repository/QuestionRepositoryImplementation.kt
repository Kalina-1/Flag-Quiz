package com.example.flagquiz.repository

import com.example.flagquiz.repository.QuestionRepository
import com.example.flagquiz.R
import com.example.flagquiz.model.Question

class QuestionRepositoryImpl : QuestionRepository {

    // Sample data, replace with actual data fetching logic (e.g., from API or local DB)
    override suspend fun getQuestions(): List<Question> {
        return listOf(
            Question(
                flagImage = com.example.flagquiz.R.drawable.nepal,  // Make sure 'nepal.png' is in the 'res/drawable' folder
                options = listOf("Spain", "Canada", "Nepal", "India"),
                correctAnswer = "Nepal",
                capitalHint = "Kathmandu"
            ),
            Question(
                flagImage = com.example.flagquiz.R.drawable.usa,  // Make sure 'usa.png' is in the 'res/drawable' folder
                options = listOf("USA", "Mexico", "Brazil", "Argentina"),
                correctAnswer = "USA",
                capitalHint = "Washington D.C."
            )
        )
    }
}