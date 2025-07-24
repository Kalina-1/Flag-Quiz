package com.example.flagquiz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flagquiz.model.User
import com.example.flagquiz.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    // To observe login result (success/fail + message)
    private val _loginStatus = MutableLiveData<Pair<Boolean, String>>()
    val loginStatus: LiveData<Pair<Boolean, String>> = _loginStatus

    // To observe registration result (success/fail + message + user data)
    private val _registerStatus = MutableLiveData<Triple<Boolean, String, User?>>()
    val registerStatus: LiveData<Triple<Boolean, String, User?>> = _registerStatus

    // Function to login user using email & password
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repo.signIn(email, password)
            result.onSuccess { firebaseUser ->
                firebaseUser?.uid?.let { uid ->
                    val userDetailsResult = repo.getUser(uid)
                    userDetailsResult.onSuccess {
                        _loginStatus.postValue(Pair(true, "Login Successful!"))
                    }.onFailure {
                        _loginStatus.postValue(Pair(false, it.message ?: "Login succeeded but user data not found"))
                    }
                } ?: run {
                    _loginStatus.postValue(Pair(false, "Login failed: No user found"))
                }
            }.onFailure {
                _loginStatus.postValue(Pair(false, it.message ?: "Login failed"))
            }
        }
    }

    // Function to register a new user with fullName, email, country, password
    fun register(fullName: String, email: String, country: String, password: String) {
        viewModelScope.launch {
            val result = repo.signUp(email, password)
            result.onSuccess { firebaseUser ->
                firebaseUser?.let { user ->
                    // Create User object from provided details
                    val newUser = User(
                        uid = user.uid,
                        username = fullName,
                        email = email,
                        firstName = fullName.split(" ").firstOrNull(),
                        lastName = fullName.split(" ").drop(1).joinToString(" "),
                        address = country
                    )
                    val saveResult = repo.saveUser(newUser)
                    saveResult.onSuccess {
                        _registerStatus.postValue(Triple(true, "Registration Successful and data saved!", newUser))
                    }.onFailure {
                        _registerStatus.postValue(Triple(false, it.message ?: "Failed to save user data", null))
                    }
                } ?: run {
                    _registerStatus.postValue(Triple(false, "Registration failed: No user created.", null))
                }
            }.onFailure {
                _registerStatus.postValue(Triple(false, it.message ?: "Registration failed", null))
            }
        }
    }
}
