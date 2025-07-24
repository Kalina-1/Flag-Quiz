package com.example.flagquiz.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagquiz.viewmodel.HighScoresViewModel

@Composable
fun HighScoresScreen(viewModel: HighScoresViewModel) {
    // Main vertical layout container, fills the screen with padding
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        // Center everything horizontally
        horizontalAlignment = Alignment.CenterHorizontally,
        // Center content vertically as well
        verticalArrangement = Arrangement.Center
    ) {
        // Title text for the screen
        Text(text = "High Scores", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(20.dp)) // Space between title and scores

        // Loop through the high scores from the ViewModel and display each
        viewModel.highScores.value.forEach { score ->
            Text(text = score, fontSize = 18.sp) // Show each score with a readable font size
        }
    }
}
