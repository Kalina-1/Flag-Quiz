package com.example.flagquiz.repository

import com.example.flagquiz.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import java.lang.Void // Keep this import

class UserRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
) {

    private val usersRef = database.getReference("users")

    suspend fun saveUser(user: User): Result<Void?> {
        return try {
            val userId = user.uid ?: return Result.failure(Exception("User UID is null"))
            usersRef.child(userId).setValue(user).await()
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(uid: String): Result<User> {
        return try {
            val snapshot = usersRef.child(uid).get().await()
            val user = snapshot.getValue(User::class.java)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- IMPORTANT: Ensure these two methods are present ---
    suspend fun signUp(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(authResult.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(authResult.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    // --------------------------------------------------------

    fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }

    fun signOut() {
        auth.signOut()
    }
}