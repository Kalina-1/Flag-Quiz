package com.example.flagquiz.model

data class FlagQuestion(
    val id: Int,
    val flagResId: Int,
    val options: List<String>,
    val correctAnswer: String
)