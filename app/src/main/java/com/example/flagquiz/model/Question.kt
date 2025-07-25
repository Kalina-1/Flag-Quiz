package com.example.flagquiz.model

// This class represents a quiz question.
// It contains information about the flag image,
// a list of possible answers, the correct answer,
// and a hint about the capital city of the country represented by the flag.
// This makes it easier to organize and share all the details of a question together.

data class Question(
    val flagImage: Int,
    val options: List<String>,
    val correctAnswer: String,
    val capitalHint: String
)
