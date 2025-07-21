package com.example.flagquiz.pages

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.* // Ensure this includes OutlinedTextField and its related components
import androidx.compose.runtime.* // IMPORTANT: Import for remember, mutableStateOf, LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagquiz.view.LoginActivity

// Add Firebase imports
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.util.Log // IMPORTANT: Import for Log.e

@OptIn(ExperimentalMaterial3Api::class) // Needed for OutlinedTextField
@Composable
fun SettingsScreen(onSignOut: () -> Unit) {
    val context = LocalContext.current

    // Initialize Firebase
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    val currentUser = auth.currentUser

    // State for editable fields
    var usernameState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }

    // Load initial data from Firebase when the screen first composes
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            val userRef = database.getReference("users").child(currentUser.uid)
            userRef.get().addOnSuccessListener { dataSnapshot ->
                usernameState = dataSnapshot.child("username").getValue(String::class.java) ?: currentUser.displayName ?: "Guest"
                emailState = dataSnapshot.child("email").getValue(String::class.java) ?: currentUser.email ?: "Not provided"
            }.addOnFailureListener {
                // Handle error loading data from Firebase
                Toast.makeText(context, "Failed to load user data: ${it.message}", Toast.LENGTH_SHORT).show()
                Log.e("SettingsScreen", "Error loading user data from Firebase", it)

                // Fallback to SharedPreferences if Firebase fails to load
                val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
                usernameState = sharedPreferences.getString("fullName", "Guest") ?: "Guest"
                emailState = sharedPreferences.getString("email", "Not provided") ?: "Not provided"
            }
        } else {
            // If no user is logged in, use SharedPreferences as a default
            val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
            usernameState = sharedPreferences.getString("fullName", "Guest") ?: "Guest"
            emailState = sharedPreferences.getString("email", "Not provided") ?: "Not provided"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFFCC99).copy(alpha = 0.8f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Settings", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(20.dp))

        SettingsSection(title = "User Info") {
            // IMPORTANT: Wrap the input fields in a Column or similar layout
            Column {
                // Editable Username
                OutlinedTextField(
                    value = usernameState,
                    onValueChange = { usernameState = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFF97B57),
                        unfocusedBorderColor = Color(0xFFB25935),
                        focusedLabelColor = Color(0xFFF97B57)
                    )
                )
                // Editable Email
                OutlinedTextField(
                    value = emailState,
                    onValueChange = { emailState = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFF97B57),
                        unfocusedBorderColor = Color(0xFFB25935),
                        focusedLabelColor = Color(0xFFF97B57)
                    )
                )

                // --- SAVE BUTTON ---
                Button(
                    onClick = {
                        if (currentUser != null) {
                            val userProfile = mapOf(
                                "username" to usernameState,
                                "email" to emailState
                            )
                            database.getReference("users").child(currentUser.uid).setValue(userProfile)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Failed to update profile: ${e.message}", Toast.LENGTH_LONG).show()
                                    Log.e("SettingsScreen", "Error updating profile", e) // Log the error for debugging
                                }
                        } else {
                            Toast.makeText(context, "No user logged in to update profile.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Save Profile", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            } // End of inner Column
        } // End of SettingsSection for User Info

        Spacer(modifier = Modifier.height(20.dp))

        SettingsSection(title = "App Information") {
            AboutAppSection()
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                Toast.makeText(context, "Logging out...", Toast.LENGTH_SHORT).show()
                onSignOut()
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(70.dp)
                .padding(vertical = 20.dp)
                .background(Color(0xFFF97B57))
                .border(2.dp, Color(0xFFB25935), RoundedCornerShape(8.dp)),
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
        content() // This is where the inner content (like our OutlinedTextFields) gets placed
    }
}

// UserInfoCard is likely no longer needed if you're directly using OutlinedTextFields
// You can remove it or keep it if it's used elsewhere.
// @Composable
// fun UserInfoCard(label: String, value: String) {
//     Card(
//         modifier = Modifier
//             .fillMaxWidth()
//             .padding(vertical = 8.dp),
//         shape = RoundedCornerShape(12.dp),
//         colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
//     ) {
//         Row(
//             modifier = Modifier
//                 .fillMaxWidth()
//                 .padding(16.dp),
//             verticalAlignment = Alignment.CenterVertically
//         ) {
//             Text(text = "$label: ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
//             Spacer(modifier = Modifier.width(8.dp))
//             Text(text = value, fontSize = 16.sp)
//         }
//     }
// }

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
    SettingsScreen(onSignOut = {})
}

