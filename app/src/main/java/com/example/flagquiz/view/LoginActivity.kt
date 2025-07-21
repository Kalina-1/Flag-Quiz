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
import com.example.flagquiz.model.User

class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        setContent {
            FlagQuizTheme {
                LoginBody(auth, database)
            }
        }
    }
}

@Composable
fun LoginBody(auth: FirebaseAuth, database: FirebaseDatabase) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    Icon(
                        painter = painterResource(
                            if (showPassword) R.drawable.baseline_visibility_24
                            else R.drawable.baseline_visibility_off_24
                        ),
                        contentDescription = "Toggle password visibility",
                        modifier = Modifier.clickable { showPassword = !showPassword }
                    )
                },
                visualTransformation = if (showPassword) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF673AB7))
                    )
                    Text("Remember Me", fontSize = 12.sp)
                }
                TextButton(onClick = { }) {
                    Text("Forgot Password?", color = Color(0xFF673AB7), fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(context as Activity) { task ->
                            if (task.isSuccessful) {
                                val firebaseUser = auth.currentUser
                                firebaseUser?.let { user ->
                                    val userId = user.uid
                                    database.getReference("users").child(userId).get()
                                        .addOnSuccessListener { dataSnapshot ->
                                            val userFromDb = dataSnapshot.getValue(User::class.java)
                                            if (userFromDb != null) {
                                                val editor = sharedPreferences.edit()
                                                editor.putString("uid", userFromDb.uid)
                                                editor.putString("fullName", userFromDb.username)
                                                editor.putString("email", userFromDb.email)
                                                editor.putString("country", userFromDb.address)
                                                editor.apply()
                                                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                                                context.startActivity(Intent(context, NavigationActivity::class.java))
                                                (context as? Activity)?.finish()
                                            } else {
                                                val newUser = User(
                                                    uid = userId,
                                                    username = "User",
                                                    email = email,
                                                    address = "Not Provided"
                                                )
                                                database.getReference("users").child(userId).setValue(newUser)
                                                    .addOnSuccessListener {
                                                        val editor = sharedPreferences.edit()
                                                        editor.putString("uid", newUser.uid)
                                                        editor.putString("fullName", newUser.username)
                                                        editor.putString("email", newUser.email)
                                                        editor.putString("country", newUser.address)
                                                        editor.apply()
                                                        Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                                                        context.startActivity(Intent(context, NavigationActivity::class.java))
                                                        (context as? Activity)?.finish()
                                                    }
                                                    .addOnFailureListener {
                                                        Toast.makeText(context, "Failed to create user profile: ${it.message}", Toast.LENGTH_SHORT).show()
                                                        auth.signOut()
                                                    }
                                            }
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Failed to fetch user data: ${it.message}", Toast.LENGTH_SHORT).show()
                                            auth.signOut()
                                        }
                                }
                            } else {
                                Toast.makeText(context, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth(),
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
                        (context as? Activity)?.finish()
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
        LoginBody(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance())
    }
}
