package com.miapp.xanostorekotlin.api // Paquete de configuración de API

import com.miapp.xanostorekotlin.BuildConfig // Import de constantes generadas por Gradle (BuildConfig)

/**
 * ApiConfig centraliza la lectura de las URLs base desde BuildConfig.
 * Estas variables se declaran en app/build.gradle.kts como buildConfigField.
 *
 * - XANO_STORE_BASE: Base de la API de productos
 * - XANO_AUTH_BASE:  Base de la API de autenticación
 * - XANO_TOKEN_TTL_SEC: TTL de respaldo para tokens JWE
 */
object ApiConfig { // Objeto singleton con configuración básica de API
    val storeBaseUrl: String = "https://x8ki-letl-twmt.n7.xano.io/api:6Vhgj82e/" // URL base para endpoints de tienda/productos
    val authBaseUrl: String = "https://x8ki-letl-twmt.n7.xano.io/api:PxLo7NSi/" // URL base para endpoints de autenticación
    val tokenTtlSec: Int = 86400 // Tiempo de vida (TTL) en segundos para tokens simulados


}