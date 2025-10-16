package com.miapp.xanostorekotlin.model // Paquete de modelos de datos

/**
 * Product
 * Modelo de datos que representa un producto.
 * Coincide con la respuesta real de la API de Xano y la estructura de la base de datos.
 */
data class Product(
    val id: Int,                           // Identificador único del producto
    val created_at: String,                // Fecha de creación (timestamp como String)
    val title: String,                     // Título del producto/libro
    val author: String,                    // Autor
    val genre: String,                     // Género
    val description: String?,              // Descripción (puede ser nula)
    val price: Double,                     // Precio
    val stock: Int,                        // Stock disponible
    val image: List<ProductImage>?         // Lista de imágenes asociadas (puede ser nula)
) : java.io.Serializable

/**
 * ProductImage
 * Modelo de datos para el objeto anidado dentro de la lista "image".
 * Representa una única imagen asociada a un producto.
 */