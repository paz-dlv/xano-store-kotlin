package com.miapp.xanostorekotlin.api

import com.miapp.xanostorekotlin.model.CreateProductRequest
import com.miapp.xanostorekotlin.model.Product
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

/**
 * ProductService
 * Interfaz de Retrofit para obtener y crear productos usando corrutinas.
 * Los métodos devuelven directamente los modelos, NO Call<>.
 */
interface ProductService {
    @GET("products")
    suspend fun getProducts(): List<Product> // Devuelve una lista de productos

    @POST("products")
    suspend fun addProduct(@Body product: CreateProductRequest): Product // Crea y devuelve el producto creado
}