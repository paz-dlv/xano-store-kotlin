package com.miapp.xanostorekotlin.model

data class RegisterUserResponse(
    val authToken: String,
    val user: User
)
