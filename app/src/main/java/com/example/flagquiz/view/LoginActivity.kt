package com.example.flagquiz.view

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flagquiz.R
import com.example.flagquiz.ui.theme.FlagQuizTheme
import com.example.flagquiz.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FlagQuizTheme {
                val loginViewModel: LoginViewModel = viewModel()
                LoginBody(viewModel = loginViewModel)
            }
        }
    }
}

@Composable
fun LoginBody(viewModel: LoginViewModel) {
    val context = LocalContext.current
    val loginError by remember { viewModel.loginError }
    val isAuthenticated by remember { viewModel.isAuthenticated }

    // State for Remember Me checkbox
    var isRememberMeChecked by remember { mutableStateOf(false) }

    // State for password visibility
    var showPassword by remember { mutableStateOf(false) }

    // Show loading or success UI based on authentication
    if (isAuthenticated) {
        // Navigate to the next screen (after login is successful)
        context.startActivity(Intent(context, NavigationActivity::class.java))
        (context as? ComponentActivity)?.finish()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.flags),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFCC99).copy(alpha = 0.8f))
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = viewModel.email.value,
                onValueChange = { viewModel.email.value = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
                    .testTag("login_email_field")
            )

            OutlinedTextField(
                value = viewModel.password.value,
                onValueChange = { viewModel.password.value = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    // Show different icons based on the password visibility state
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
                modifier = Modifier.fillMaxWidth()
                    .testTag("login_password_field")
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Remember Me Checkbox
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isRememberMeChecked,
                        onCheckedChange = { isRememberMeChecked = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF673AB7))
                    )
                    Text("Remember Me", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            loginError?.let {
                Text(text = it, color = Color.Red, fontSize = 14.sp)
            }

            Button(
                onClick = {
                    viewModel.login() // Trigger login in ViewModel
                },
                modifier = Modifier.fillMaxWidth()
                    .testTag("login_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Log In", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color.LightGray, thickness = 1.dp)
                Text("Or", color = Color.Gray, fontSize = 12.sp)
                Divider(modifier = Modifier.weight(1f), color = Color.LightGray, thickness = 1.dp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account? ", color = Color.Black, fontSize = 12.sp)
                Text(
                    "Sign Up",
                    color = Color(0xFF673AB7),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        val intent = Intent(context, SignupActivity::class.java)
                        context.startActivity(intent)
                        (context as? ComponentActivity)?.finish()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    FlagQuizTheme {
        LoginBody(viewModel = LoginViewModel())
    }
}
