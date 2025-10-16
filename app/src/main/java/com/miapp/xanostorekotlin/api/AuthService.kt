package com.miapp.xanostorekotlin.api

import com.miapp.xanostorekotlin.model.LoginRequest
import com.miapp.xanostorekotlin.model.LoginResponse
import com.miapp.xanostorekotlin.model.RegisterUserRequest
import com.miapp.xanostorekotlin.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {
    // Login con coroutines (suspend)
    @POST("auth/login")
    suspend fun loginSuspend(@Body request: LoginRequest): LoginResponse

    // Login con callback (enqueue)
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("auth/me")
    suspend fun getMe(): User

    @POST("auth/signup")
    fun signUp(@Body request: RegisterUserRequest): Call<User>
}