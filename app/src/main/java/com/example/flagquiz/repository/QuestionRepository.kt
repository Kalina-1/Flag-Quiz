package com.example.flagquiz.repository

import com.example.flagquiz.model.Question


interface QuestionRepository {
    suspend fun getQuestions(): List<Question>
}