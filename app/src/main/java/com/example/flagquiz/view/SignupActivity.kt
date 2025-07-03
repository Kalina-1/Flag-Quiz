package com.example.flagquiz.view

import android.app.Activity
import android.content.Context
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

class SignupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlagQuizTheme {
                Scaffold { innerPadding ->
                    val sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE)
                    val context = this

                    SignupBody(
                        innerPadding = innerPadding,
                        onRegisterAttempt = { fullName, email, country, password, termsAccepted ->
                            if (!termsAccepted) {
                                Toast.makeText(this, "Please accept terms and privacy policy.",
                                    Toast.LENGTH_SHORT).show()
                                return@SignupBody
                            }
                            if (country.isBlank() || country == "Select Country") {
                                Toast.makeText(this, "Please select a country.",
                                    Toast.LENGTH_SHORT).show()
                                return@SignupBody
                            }

                            val localEmail: String? = sharedPreferences.getString("email", "")
                            if (localEmail == email) {
                                Toast.makeText(
                                    this,
                                    "This email is already registered",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val editor = sharedPreferences.edit()
                                editor.putString("fullName", fullName)
                                editor.putString("email", email)
                                editor.putString("country", country)
                                editor.putString("password", password)
                                editor.apply()
                                Toast.makeText(
                                    this,
                                    "Signup Successful",
                                    Toast.LENGTH_SHORT
                                ).show()


                                val intent = Intent(context, DashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        },
                        onLoginClick = {
                            Toast.makeText(this, "Navigate to Login screen", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
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
    onRegisterAttempt: (fullName: String, email: String, country: String, password: String, termsAccepted: Boolean) -> Unit,
    onLoginClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var reenterPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showReenterPassword by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf("Select Country") }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val countries = listOf("USA", "Canada", "UK", "Australia", "Germany", "France", "Japan", "Nepal")

    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.flags),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 0.dp)
                .background(Color(0xFFFFCC99).copy(alpha = 0.8f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57)),
                    shape = RoundedCornerShape(8.dp),
                    enabled = false
                ) {
                    Text("Signup", color = Color.Black)
                }
                OutlinedButton(
                    onClick = onLoginClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Log In", color = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
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
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    val visibilityIcon = if (showPassword) R.drawable.baseline_visibility_24
                    else R.drawable.baseline_visibility_off_24
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
            OutlinedTextField(
                value = reenterPassword,
                onValueChange = { reenterPassword = it },
                label = { Text("Re-enter Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    val visibilityIcon = if (showReenterPassword) R.drawable.baseline_visibility_24
                    else R.drawable.baseline_visibility_off_24
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
                    shape = RoundedCornerShape(8.dp)
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = { termsAccepted = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFFF97B57))
                )
                Text(
                    "I agree to Terms and Privacy Policy.",
                    fontSize = 14.sp
                )
            }
            Button(
                onClick = {
                    if (password == reenterPassword) {
                        onRegisterAttempt(fullName, email, selectedCountry, password, termsAccepted)
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Already have an account? ",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Text(
                    "Log In",
                    color = Color(0xFF673AB7),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onLoginClick()
                        val intent = Intent(context, DashboardActivity::class.java)
                        context.startActivity(intent)
                        (context as? Activity)?.finish()
                    }
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
            onRegisterAttempt = { _, _, _, _, _ -> },
            onLoginClick = { }
        )
    }
}