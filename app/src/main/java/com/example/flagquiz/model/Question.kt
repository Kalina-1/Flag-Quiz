package com.example.flagquiz.model

// This class represents a quiz question.
// It contains information about the flag image,
// a list of possible answers, the correct answer,
// and a hint about the capital city of the country represented by the flag.
// the correct answer itself, and a little hint about the capital city.
// Makes it easy to pass around all question details together.
data class Question(
    val flagImage: Int,
    val options: List<String>,
    val correctAnswer: String,
    val capitalHint: String
)
