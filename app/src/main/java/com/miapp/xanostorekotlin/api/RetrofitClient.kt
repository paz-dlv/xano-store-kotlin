package com.miapp.xanostorekotlin.api

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Configuración básica de OkHttpClient con logging y timeouts
    private fun baseOkHttpBuilder(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
    }

    // Instancia de Retrofit usando el cliente y la baseUrl
    private fun retrofit(baseUrl: String, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // Servicio de autenticación (login/register)
    fun createAuthService(context: Context, baseUrl: String, requiresAuth: Boolean = false): AuthService {
        val clientBuilder = baseOkHttpBuilder()
        if (requiresAuth) {
            val tokenManager = TokenManager(context)
            clientBuilder.addInterceptor(AuthInterceptor { tokenManager.getToken() })
        }
        val client = clientBuilder.build()
        return retrofit(baseUrl, client).create(AuthService::class.java)
    }

    // Servicio para productos
    fun createProductService(context: Context, baseUrl: String): ProductService {
        val tokenManager = TokenManager(context)
        val client = baseOkHttpBuilder()
            .addInterceptor(AuthInterceptor { tokenManager.getToken() })
            .build()
        return retrofit(baseUrl, client).create(ProductService::class.java)
    }

    // Servicio para subir imágenes (opcional, solo si lo requieres)
    fun createUploadService(context: Context, baseUrl: String): UploadService {
        val tokenManager = TokenManager(context)
        val client = baseOkHttpBuilder()
            .addInterceptor(AuthInterceptor { tokenManager.getToken() })
            .build()
        return retrofit(baseUrl, client).create(UploadService::class.java)
    }
}