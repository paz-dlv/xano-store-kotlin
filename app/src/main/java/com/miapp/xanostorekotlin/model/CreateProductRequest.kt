package com.miapp.xanostorekotlin.model


/**
 * CreateProductRequest: Representa la estructura del cuerpo JSON para la petición POST a /product.
 * Solo incluye los campos que el usuario puede subir en la app.
 *
 * JSON generado:
 * {
 *   "title": "El Principito",
 *   "author": "Antoine de Saint-Exupéry",
 *   "genre": "Novela",
 *   "description": "Obra clásica de la literatura",
 *   "price": 2000,
 *   "stock": 10,
 *   "image": [ { ... } ]
 * }
 */
data class CreateProductRequest(
    val title: String,                // Título del producto/libro
    val author: String,               // Autor
    val genre: String,                // Género
    val description: String?,         // Descripción
    val price: Double,                // Precio (decimal, no int)
    val stock: Int,                   // Stock disponible
    val image: List<ProductImage>?    // Imágenes (array de objetos ProductImage)
)