package com.miapp.xanostorekotlin.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("created_at")
    val createdAt: String, // O String si Xano devuelve un ISO timestamp

    @SerializedName("lastname")
    val lastname: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("shipping_address")
    val shippingAddress: String,

    @SerializedName("phone")
    val phone: String
)