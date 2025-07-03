package com.example.flagquiz.pages

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.flagquiz.view.QuizScreenActivity

@Composable
fun HomeScreen() {
    // Correctly get the context using LocalContext
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome to the Flag Quiz App!", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))

        // Start New Quiz button with Toast and delay before opening the QuizActivity
        Button(
            onClick = {
                // Show Toast when button is clicked
                Toast.makeText(context, "Starting Quiz...", Toast.LENGTH_SHORT).show()

                // Introduce a 2-second delay before transitioning to QuizActivity
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(context, QuizScreenActivity::class.java) // Navigate to QuizActivity
                    context.startActivity(intent)
                }, 2000) // Delay of 2 seconds
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57))  // Orange button color
        ) {
            Text("Start New Quiz", color = Color.White)
        }
        Spacer(modifier = Modifier.height(20.dp))

        // View High Scores button with orange color (functionality to be added later)
        Button(
            onClick = { /* View High Scores logic */ },
            modifier = Modifier.fillMaxWidth(0.8f).height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57)) // Orange button color
        ) {
            Text(text = "View High Scores", color = Color.White)
        }
    }
}