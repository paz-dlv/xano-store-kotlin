package com.miapp.xanostorekotlin.model


data class CartDto(
    val id: Int,
    val created_at: Long,
    val user_id: Int?
)