package com.example.flagquiz.view

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import com.example.flagquiz.R
import com.example.flagquiz.ui.theme.FlagQuizTheme
import com.example.flagquiz.ui.theme.FlagQuizTheme

class ForgetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlagQuizTheme {
                ForgetPasswordBody()
            }
        }
    }
}

@Composable
fun ForgetPasswordBody() {
    var email by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as? Activity

    // Function to validate the email format
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image and color
        Image(
            painter = painterResource(R.drawable.flags),  // Make sure the image resource exists
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Main content with semi-transparent background
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFCC99).copy(alpha = 0.8f))  // Background color with transparency
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text("Reset Your Password", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(30.dp))

            // Email input field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                isError = emailError.isNotEmpty()
            )

            // Email validation error message
            if (emailError.isNotEmpty()) {
                Text(
                    text = emailError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Reset Password Button
            Button(
                onClick = {
                    if (email.isEmpty()) {
                        Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                    } else if (!isValidEmail(email)) {
                        emailError = "Please enter a valid email address."
                    } else {
                        isLoading = true
                        showDialog = true
                        emailError = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57)),  // Updated button color
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Send Reset Link")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Back to login button
            Text(
                "Back to Login",
                modifier = Modifier.clickable {
                    activity?.finish()
                },
                color = Color.Blue
            )
        }

        // Dialog on successful email submission
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false
                        Toast.makeText(context, "Reset link sent to $email", Toast.LENGTH_SHORT).show()
                        activity?.finish()
                    }) {
                        Text("OK")
                    }
                },
                title = { Text("Password Reset") },
                text = { Text("A password reset link has been sent to your email.") }
            )
        }
    }
}

@Preview
@Composable
fun ForgetPasswordPreview() {
    FlagQuizTheme{
        ForgetPasswordBody()
    }
}