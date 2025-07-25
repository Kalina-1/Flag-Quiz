package com.example.flagquiz.view

import android.app.Activity
import android.content.Intent
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagquiz.R
import com.example.flagquiz.ui.theme.FlagQuizTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.flagquiz.view.NavigationActivity
import com.example.flagquiz.view.LoginActivity
import com.example.flagquiz.model.User

class SignupActivity : ComponentActivity() {

    // Firebase authentication and realtime database instances
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // removes status bar space for full UI experience

        // initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        setContent {
            FlagQuizTheme {
                Scaffold { innerPadding ->
                    val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
                    val context = this

                    // Building the signup screen with input validations and Firebase logic
                    SignupBody(
                        innerPadding = innerPadding,
                        onRegisterAttempt = { fullName, email, country, password ->

                            // just some quick sanity checks before trying to sign up
                            if (fullName.isBlank()) {
                                Toast.makeText(context, "Please enter your full name.", Toast.LENGTH_SHORT).show()
                                return@SignupBody
                            }

                            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                Toast.makeText(context, "Please enter a valid email.", Toast.LENGTH_SHORT).show()
                                return@SignupBody
                            }

                            if (password.length < 6) {
                                Toast.makeText(context, "Password should be at least 6 characters.", Toast.LENGTH_SHORT).show()
                                return@SignupBody
                            }

                            if (country.isBlank() || country == "Select Country") {
                                Toast.makeText(context, "Please select a country.", Toast.LENGTH_SHORT).show()
                                return@SignupBody
                            }

                            // finally, try to register using Firebase Auth
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        val firebaseUser = auth.currentUser
                                        firebaseUser?.let { user ->

                                            // used to split full name to store first and last separately
                                            val nameParts = fullName.trim().split(" ")

                                            val newUser = User(
                                                uid = user.uid,
                                                username = fullName,
                                                email = email,
                                                firstName = nameParts.firstOrNull() ?: "",
                                                lastName = if (nameParts.size > 1) nameParts.drop(1).joinToString(" ") else "",
                                                address = country
                                            )

                                            // save extra user info in Firebase Realtime DB
                                            database.getReference("users").child(user.uid).setValue(newUser)
                                                .addOnCompleteListener { dbTask ->
                                                    if (dbTask.isSuccessful) {
                                                        // store locally as well (optional but useful)
                                                        val editor = sharedPreferences.edit()
                                                        editor.putString("fullName", fullName)
                                                        editor.putString("email", email)
                                                        editor.putString("country", country)
                                                        editor.apply()

                                                        Toast.makeText(context, "Signup successful!", Toast.LENGTH_SHORT).show()

                                                        // go to main screen
                                                        startActivity(Intent(context, NavigationActivity::class.java))
                                                        finish()
                                                    } else {
                                                        Toast.makeText(context, "Failed to save user data: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                        }
                                    } else {
                                        Toast.makeText(context, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        },
                        onLoginClick = {
                            // navigate to login screen
                            startActivity(Intent(context, LoginActivity::class.java))
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupBody(
    innerPadding: PaddingValues,
    onRegisterAttempt: (fullName: String, email: String, country: String, password: String) -> Unit,
    onLoginClick: () -> Unit
) {
    // form states (local to UI only)
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var reenterPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showReenterPassword by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf("Select Country") }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val countries = listOf("USA", "Canada", "UK", "Australia", "Germany", "France", "Japan", "Nepal")

    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        // background image for style
        Image(
            painter = painterResource(id = R.drawable.flags),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(Color(0xFFFFCC99).copy(alpha = 0.8f)), // added a soft background overlay
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // Full Name Field
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    val visibilityIcon = if (showPassword) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
                    Icon(
                        painter = painterResource(id = visibilityIcon),
                        contentDescription = "Toggle password visibility",
                        modifier = Modifier.clickable { showPassword = !showPassword }
                    )
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Re-enter Password Field
            OutlinedTextField(
                value = reenterPassword,
                onValueChange = { reenterPassword = it },
                label = { Text("Re-enter Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    val visibilityIcon = if (showReenterPassword) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
                    Icon(
                        painter = painterResource(id = visibilityIcon),
                        contentDescription = "Toggle re-enter password visibility",
                        modifier = Modifier.clickable { showReenterPassword = !showReenterPassword }
                    )
                },
                visualTransformation = if (showReenterPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Country Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCountry,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Country") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = if (selectedCountry == "Select Country") Color.Red else Color.Gray,
                        unfocusedBorderColor = if (selectedCountry == "Select Country") Color.Red else Color.Gray
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    countries.forEach { country ->
                        DropdownMenuItem(
                            text = { Text(country) },
                            onClick = {
                                selectedCountry = country
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Signup Button
            Button(
                onClick = {
                    if (password == reenterPassword) {
                        onRegisterAttempt(fullName, email, selectedCountry, password)
                    } else {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Signup", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login redirect
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Already have an account? ", color = Color.Black, fontSize = 14.sp)
                Text(
                    "Log In",
                    color = Color(0xFF673AB7),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupPreview() {
    FlagQuizTheme {
        SignupBody(
            innerPadding = PaddingValues(0.dp),
            onRegisterAttempt = { _, _, _, _ -> },
            onLoginClick = { }
        )
    }
}
