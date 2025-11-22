package com.miapp.xanostorekotlin.api

import com.miapp.xanostorekotlin.model.CartDto
import com.miapp.xanostorekotlin.model.CartItemDto
import retrofit2.Response
import retrofit2.http.*

interface CartService {

    // Obtener carts (puedes filtrar por user_id usando query param)
    @GET("cart")
    suspend fun getCarts(@Query("user_id") userId: Int? = null): List<CartDto>

    // Crear cart (body: { "user_id": 123 })
    @POST("cart")
    suspend fun createCart(@Body body: Map<String, @JvmSuppressWildcards Any>): CartDto

    // Obtener items del carrito (filter por cart_id)
    @GET("cart_item")
    suspend fun getCartItems(@Query("cart_id") cartId: Int? = null): List<CartItemDto>

    @POST("cart_item")
    suspend fun createCartItem(@Body body: Map<String, @JvmSuppressWildcards Any>): CartItemDto

    @PUT("cart_item/{id}")
    suspend fun updateCartItem(@Path("id") id: Int, @Body body: Map<String, @JvmSuppressWildcards Any>): CartItemDto

    @DELETE("cart_item/{id}")
    suspend fun deleteCartItem(@Path("id") id: Int): Response<Unit>
}