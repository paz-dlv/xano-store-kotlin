package com.miapp.xanostorekotlin.api

import android.content.Context
import android.content.SharedPreferences


class TokenManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Token en memoria
    var currentToken: String? = null
        private set

    init {
        currentToken = prefs.getString(KEY_TOKEN, null)
    }

    /**
     * Guardar datos de sesión.
     * Ahora incluye opcionalmente userId (Integer) para facilitar llamadas que requieren user_id.
     */
    fun saveAuth(token: String, userName: String, userEmail: String, userId: Int? = null) {
        currentToken = token
        prefs.edit().apply {
            putString(KEY_TOKEN, token)
            putString(KEY_USER_NAME, userName)
            putString(KEY_USER_EMAIL, userEmail)
            if (userId != null) putInt(KEY_USER_ID, userId) else remove(KEY_USER_ID)
            apply()
        }
    }

    fun getToken(): String? = currentToken

    fun getUserName(): String? = prefs.getString(KEY_USER_NAME, null)
    fun getUserEmail(): String? = prefs.getString(KEY_USER_EMAIL, null)

    /**
     * Nuevo método: devuelve el user id almacenado (o null si no está).
     */
    fun getUserId(): Int? {
        return if (prefs.contains(KEY_USER_ID)) prefs.getInt(KEY_USER_ID, -1).takeIf { it >= 0 } else null
    }

    fun isLoggedIn(): Boolean = getToken() != null

    fun clear() {
        currentToken = null
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "session"
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_ID = "user_id"
    }
}