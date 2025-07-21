package com.example.flagquiz.model

data class Question(
    val flagImage: Int,
    val options: List<String>,
    val correctAnswer: String,
    val capitalHint: String
)