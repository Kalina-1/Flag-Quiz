package com.example.flagquiz.model

// This class holds information about a user in the app.

data class User(
    var uid: String? = null, // The unique identifier for the user
    var username: String? = null, // The username chosen by the user
    var email: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var address: String? = null
)