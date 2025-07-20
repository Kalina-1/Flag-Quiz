
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "High Scores", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(20.dp))

        // Display High Scores
        viewModel.highScores.value.forEach { score ->
            Text(text = score, fontSize = 18.sp)
        }
    }
}
