package com.example.flagquiz.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.flagquiz.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginViewModel : ViewModel() {
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var loginError = mutableStateOf<String?>(null)
    var isAuthenticated = mutableStateOf(false)

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun login() {
        auth.signInWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update isAuthenticated on successful login
                    isAuthenticated.value = true

                    // Save user data if necessary
                    val firebaseUser = auth.currentUser
                    firebaseUser?.let { user ->
                        val userId = user.uid
                        database.getReference("users").child(userId).get()
                            .addOnSuccessListener { dataSnapshot ->
                                val userFromDb = dataSnapshot.getValue(User::class.java)
                                if (userFromDb != null) {
                                    // Successfully fetched user data, handle navigation
                                }
                            }
                            .addOnFailureListener {
                                loginError.value = "Failed to fetch user data"
                            }
                    }
                } else {
                    loginError.value = "Authentication failed: ${task.exception?.message}"
                }
            }
    }
}
