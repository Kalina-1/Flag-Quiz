package com.example.flagquiz.pages

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
//import androidx.compose.material3.icons.Icons
//import androidx.compose.material3.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagquiz.view.LoginActivity

@Composable
fun SettingsScreen() {
    // Get context using LocalContext
    val context = LocalContext.current

    // Retrieve user data from shared preferences
    val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
    val userName = sharedPreferences.getString("fullName", "Guest") ?: "Guest"
    val userEmail = sharedPreferences.getString("email", "Not provided") ?: "Not provided"

    // Settings Screen Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFFCC99).copy(alpha = 0.8f)), // Semi-transparent background for the whole screen
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Title Section
        Text(text = "Settings", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(20.dp))

        // User Info Section with rounded cards
        SettingsSection(title = "User Info") {
            UserInfoCard(label = "Username", value = userName)
            UserInfoCard(label = "Email", value = userEmail)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // App Information Section
        SettingsSection(title = "App Information") {
            AboutAppSection()
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Log Out Button with improved contrast, height, and border
        Button(
            onClick = {
                // Show Toast when button is clicked
                Toast.makeText(context, "Logging out...", Toast.LENGTH_SHORT).show()

                // Optionally clear saved data
                val editor = sharedPreferences.edit()
                editor.clear() // Clear user data
                editor.apply()

                // Navigate to Login screen (LoginActivity in this case)
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(70.dp)  // Increased height for better interaction
                .padding(vertical = 20.dp) // Added more padding to make button stand out
                .background(Color(0xFFF97B57)) // Changed to the desired orange color
                .border(2.dp, Color(0xFFB25935), RoundedCornerShape(8.dp)), // Added border to the button
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Log Out", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }



    }
}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(10.dp))
        content()
    }
}

@Composable
fun UserInfoCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$label: ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = value, fontSize = 16.sp)
        }
    }
}

@Composable
fun AboutAppSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "About the App", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Flag Quiz App allows users to test their knowledge of world flags. " +
                    "With multiple-choice questions, the app provides a fun and engaging way to learn about different countries.",
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "App Version: 1.0", fontSize = 12.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen()
}