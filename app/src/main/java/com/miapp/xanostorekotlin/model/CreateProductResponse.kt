package com.miapp.xanostorekotlin.model

import com.miapp.xanostorekotlin.model.ProductImage

/**
 * CreateProductResponse
 * Respuesta del POST de creación. Devuelve directamente el producto creado.
 * Coincide con la estructura de la base y el output de la API.
 */
data class CreateProductResponse(
    val id: Int,                         // Identificador único del producto
    val created_at: String,              // Fecha de creación (timestamp como String)
    val title: String,                   // Título del producto/libro
    val author: String,                  // Autor
    val genre: String,                   // Género
    val description: String?,            // Descripción (puede ser nula)
    val price: Double,                   // Precio
    val stock: Int,                      // Stock disponible
    val image: List<ProductImage>?       // Lista de imágenes asociadas (puede ser nula)
)