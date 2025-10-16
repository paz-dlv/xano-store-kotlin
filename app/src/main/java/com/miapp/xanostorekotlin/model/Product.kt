package com.miapp.xanostorekotlin.model // Paquete de modelos de datos

/**
 * Product
 * Modelo de datos que representa un producto.
 * ACTUALIZADO para coincidir con la respuesta real de la API de Xano.
 * La estructura ahora incluye una lista de imágenes anidadas.
 */
data class Product( // data class: genera automáticamente métodos útiles como equals, hashCode, toString, y copy.
    // Identificador único del producto, viene como un número entero del JSON.
    val id: Int,

    // Nombre del producto, viene como un string del JSON.
    val author: String,

    val genre: String,

    // Descripción del producto, puede ser nula.
    val description: String?,

    // Precio del producto, puede ser nulo. Lo definimos como Int según tu JSON.
    val price: Double?,

    // Stock disponible del producto.
    val stock: Int,

    // ¡¡CAMBIO FUNDAMENTAL!!
    // La API no devuelve 'image_url', sino una lista llamada 'images'.
    // Esta propiedad ahora es una lista de objetos 'ProductImage'. Puede ser nula si no hay imágenes.
    val images: List<ProductImage>?
) : java.io.Serializable // <-- AÑADE ESTO

/**
 * ProductImage
 * Modelo de datos para el objeto anidado dentro de la lista "images".
 * Representa una única imagen asociada a un producto.
 */