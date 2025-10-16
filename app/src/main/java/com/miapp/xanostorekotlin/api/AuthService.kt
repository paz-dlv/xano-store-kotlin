package com.miapp.xanostorekotlin.api

import com.miapp.xanostorekotlin.model.AuthResponse
import com.miapp.xanostorekotlin.model.LoginRequest
import com.miapp.xanostorekotlin.model.RegisterUserRequest // Import del modelo de registro
import com.miapp.xanostorekotlin.model.User
import retrofit2.Call // Import para métodos no suspend
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("auth/me")
    suspend fun getMe(): User

    // --- REGISTRO DE USUARIO ---
    @POST("auth/signup")
    fun signUp(@Body request: RegisterUserRequest): Call<User>
}