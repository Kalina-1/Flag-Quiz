package com.example.flagquiz.repository

import com.example.flagquiz.model.UserModel
import javax.security.auth.callback.PasswordCallback

interface UserRepository {
//    {
    // "success" : false,
    // "message": "Login failed"
    // "status": 200
    // "userId": 0000000999dx
    // }

    fun login(
        email: String,
        password: String, callback: (Boolean, String) -> Unit
    )

    //authentication ko function
    fun register(
        email: String, password: String,
        callback: (Boolean, String, String) -> Unit
    )

    //database ko function
    fun addUserToDatabase(
        userId: String, model: UserModel,
        callback: (Boolean, String) -> Unit
    )

    fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    )

    fun deleteAccount(
        userId: String,
        callback: (Boolean, String) -> Unit
    )

    fun editProfile(
        userId: String, data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    )

//    fun getCurrentUser(): FirebaseUser?

    fun getUserById(
        userId: String,
        callback: (Boolean, String, UserModel?) -> Unit
    )

    fun logout(callback: (Boolean, String) -> Unit)

}