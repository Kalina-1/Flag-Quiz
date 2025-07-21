package com.example.flagquiz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flagquiz.model.User // Corrected: Import 'User' instead of 'UserModel'
import com.example.flagquiz.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    private val _loginStatus = MutableLiveData<Pair<Boolean, String>>()
    val loginStatus: LiveData<Pair<Boolean, String>> = _loginStatus

    private val _registerStatus = MutableLiveData<Triple<Boolean, String, User?>>()
    val registerStatus: LiveData<Triple<Boolean, String, User?>> = _registerStatus

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repo.signIn(email, password) // This calls the signIn in UserRepository
            result.onSuccess { firebaseUser ->
                firebaseUser?.uid?.let { uid ->
                    val userDetailsResult = repo.getUser(uid)
                    userDetailsResult.onSuccess { user ->
                        _loginStatus.postValue(Pair(true, "Login Successful!"))
                    }.onFailure { exception ->
                        _loginStatus.postValue(Pair(false, exception.message ?: "Login successful but user data not found."))
                    }
                } ?: run {
                    _loginStatus.postValue(Pair(false, "Login failed: No user found."))
                }
            }.onFailure { exception ->
                _loginStatus.postValue(Pair(false, exception.message ?: "Login failed"))
            }
        }
    }

    fun register(fullName: String, email: String, country: String, password: String) {
        viewModelScope.launch {
            val result = repo.signUp(email, password) // This calls the signUp in UserRepository
            result.onSuccess { firebaseUser ->
                firebaseUser?.let { user ->
                    val newUser = User(
                        uid = user.uid,
                        username = fullName,
                        email = email,
                        firstName = fullName.split(" ").firstOrNull(),
                        lastName = fullName.split(" ").drop(1).joinToString(" "),
                        address = country
                    )
                    val saveResult = repo.saveUser(newUser) // This calls saveUser in UserRepository
                    saveResult.onSuccess {
                        _registerStatus.postValue(Triple(true, "Registration Successful and data saved!", newUser))
                    }.onFailure { exception ->
                        _registerStatus.postValue(Triple(false, exception.message ?: "Failed to save user data", null))
                    }
                } ?: run {
                    _registerStatus.postValue(Triple(false, "Registration failed: No user created.", null))
                }
            }.onFailure { exception ->
                _registerStatus.postValue(Triple(false, exception.message ?: "Registration failed", null))
            }
        }
    }
}