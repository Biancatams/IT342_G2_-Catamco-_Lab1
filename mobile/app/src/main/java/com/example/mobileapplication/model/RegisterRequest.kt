package com.example.mobileapplication.model

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val username: String,
    val emailAddress: String,
    val password: String
)