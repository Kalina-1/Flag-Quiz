package com.example.flagquiz.model

data class Question(
    val flagImage: Int,    // Flag image resource
    val options: List<String>,   // List of options
    val correctAnswer: String,   // Correct answer
    val capitalHint: String     // Capital city hint
)
