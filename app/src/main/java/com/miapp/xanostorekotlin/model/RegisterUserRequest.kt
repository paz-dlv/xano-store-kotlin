package com.miapp.xanostorekotlin.model

data class RegisterUserRequest(
    val name: String,
    val email: String,
    val password: String,
    val shipping_address: String,
    val phone: String
)