package com.example.mobileapplication.model

data class UserResponse(
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val emailAddress: String,
    val createdAt: String
)