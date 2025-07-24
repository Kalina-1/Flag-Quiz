
package com.example.flagquiz.repository

import com.example.flagquiz.model.Question

// Interface to get questions —
// will have different implementations depending on where questions come from (API, local, etc.)
interface QuestionRepository {
    suspend fun getQuestions(): List<Question>
}
