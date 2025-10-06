package com.miapp.xanostorekotlin.api // Paquete del servicio de autenticación

import com.miapp.xanostorekotlin.model.AuthResponse // Import del modelo de respuesta de login
import com.miapp.xanostorekotlin.model.LoginRequest // Import del modelo de request de login
import com.miapp.xanostorekotlin.model.User
import retrofit2.http.Body // Import de anotación para cuerpo de la solicitud
import retrofit2.http.GET
import retrofit2.http.POST // Import de anotación para métodoo HTTP POST

/**
 * AuthService
 * Define el endpoint de login (y potencialmente logout) de la API de Xano.
 * Base URL usada: ApiConfig.authBaseUrl
 * Todas las líneas están comentadas para fines didácticos.
 */
interface AuthService { // Interfaz de Retrofit para autenticación
    @POST("auth/login") // Endpoint POST /login
    suspend fun login(@Body request: LoginRequest): AuthResponse // Métodoo suspend que envía email/password y recibe token + user


    // ¡NUEVO! Endpoint para obtener datos del usuario autenticado
    @GET("auth/me")
    suspend fun getMe(): User // Devuelve directamente el objeto User
}
