package com.miapp.xanostorekotlin.model

import com.google.android.gms.analytics.ecommerce.Product


data class CartItemDto(
    val id: Int,
    val created_at: Long,
    val quantity: Int,
    val cart_id: Int,
    val product_id: Int,
)