package com.example.flagquiz.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HighScoresViewModel : ViewModel() {
    // Replace this with actual high scores logic from your app
    val highScores = mutableStateOf(listOf("John: 100", "Sara: 90", "Mike: 80"))
}
