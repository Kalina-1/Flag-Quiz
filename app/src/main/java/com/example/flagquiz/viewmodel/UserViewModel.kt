
package com.example.flagquiz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flagquiz.model.UserModel
import com.example.flagquiz.repository.UserRepository
//import com.google.firebase.auth.FirebaseUser

class UserViewModel(val repo: UserRepository) : ViewModel(){

    // LiveData for login status
    private val _loginStatus = MutableLiveData<Pair<Boolean, String>>()
    val loginStatus: LiveData<Pair<Boolean, String>> get() = _loginStatus

    fun login(
        email: String,
        password: String
    ){
        repo.login(email, password) { success, message ->
            _loginStatus.postValue(Pair(success, message))
        }
    }

    // Registration function
    private val _registerStatus = MutableLiveData<Triple<Boolean, String, String>>()
    val registerStatus: LiveData<Triple<Boolean, String, String>> get() = _registerStatus

    fun register(
        email: String, password: String,
        callback: (Boolean, String, String) -> Unit
    ){
        repo.register(email, password) { success, message, data ->
            _registerStatus.postValue(Triple(success, message, data))
        }
    }

    // Add User to database
    fun addUserToDatabase(
        userId: String, model: UserModel,
        callback: (Boolean, String) -> Unit
    ){
        repo.addUserToDatabase(userId, model, callback)
    }

    // Forget password
    private val _passwordResetStatus = MutableLiveData<Pair<Boolean, String>>()
    val passwordResetStatus: LiveData<Pair<Boolean, String>> get() = _passwordResetStatus

    fun forgetPassword(
        email: String
    ){
        repo.forgetPassword(email) { success, message ->
            _passwordResetStatus.postValue(Pair(success, message))
        }
    }

    // Delete Account
    fun deleteAccount(
        userId: String,
        callback: (Boolean, String) -> Unit
    ){
        repo.deleteAccount(userId, callback)
    }

    // Edit Profile
    fun editProfile(
        userId: String, data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ){
        repo.editProfile(userId, data, callback)
    }

    // Get Current User
//    fun getCurrentUser(): FirebaseUser?{
//        return repo.getCurrentUser()
//    }

    // Get User by ID
    private val _users = MutableLiveData<UserModel?>()
    val users : LiveData<UserModel?> get() = _users

    fun getUserById(userId: String){
        repo.getUserById(userId) { success, message, data ->
            if (success) {
                _users.postValue(data)
            } else {
                _users.postValue(null)
            }
        }
    }

    // Logout
    fun logout(callback: (Boolean, String) -> Unit){
        repo.logout(callback)
    }

}
