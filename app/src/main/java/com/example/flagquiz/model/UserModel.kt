package com.example.flagquiz.model

// This class holds information about a user in the app.

data class User(
    var uid: String? = null,
    var username: String? = null,
    var email: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var address: String? = null
)