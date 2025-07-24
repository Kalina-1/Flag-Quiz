package com.example.flagquiz.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HighScoresViewModel : ViewModel() {
    // Just some dummy high scores for now
    // Will replace this later with real data from the app
    val highScores = mutableStateOf(listOf("John: 100", "Sara: 90", "Mike: 80"))
}
