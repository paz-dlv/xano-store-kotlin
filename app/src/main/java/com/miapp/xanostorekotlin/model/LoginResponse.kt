package com.miapp.xanostorekotlin.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("authToken") // Cambia esto si tu backend devuelve otro nombre, por ejemplo "token" o "jwt"
    val authToken: String
)