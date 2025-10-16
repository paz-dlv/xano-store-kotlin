package com.miapp.xanostorekotlin.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.miapp.xanostorekotlin.api.ApiConfig
import com.miapp.xanostorekotlin.api.RetrofitClient
import com.miapp.xanostorekotlin.api.TokenManager
import com.miapp.xanostorekotlin.databinding.ActivityMainBinding
import com.miapp.xanostorekotlin.model.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenManager = TokenManager(this)

        // --- Chequeo de sesión por usuario guardado (registro) ---
        val prefs = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val userId = prefs.getInt("id", -1)
        val userName = prefs.getString("name", null)
        if (userId != -1 && !userName.isNullOrBlank()) {
            goToHome()
            return
        }

        // --- Chequeo de sesión por token (login) ---
        if (tokenManager.isLoggedIn()) {
            goToHome()
            return
        }

        // Si no hay sesión, muestra el login
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text?.toString()?.trim().orEmpty()
            val password = binding.etPassword.text?.toString()?.trim().orEmpty()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Completa email y password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progress.visibility = View.VISIBLE
            binding.btnLogin.isEnabled = false

            lifecycleScope.launch {
                try {
                    // CORRECCIÓN: Debes pasar el baseUrl aquí
                    val publicAuthService = RetrofitClient.createAuthService(
                        context = this@MainActivity,
                        baseUrl = ApiConfig.authBaseUrl
                    )
                    val loginResponse = withContext(Dispatchers.IO) {
                        publicAuthService.login(LoginRequest(email = email, password = password))
                    }

                    val authToken = loginResponse.authToken
                    getSharedPreferences("session", Context.MODE_PRIVATE).edit().apply {
                        putString("jwt_token", authToken)
                        apply()
                    }

                    // CORRECCIÓN: Usa el baseUrl también aquí
                    val privateAuthService = RetrofitClient.createAuthService(
                        context = this@MainActivity,
                        baseUrl = ApiConfig.authBaseUrl // Si necesitas token, configura el interceptor en RetrofitClient
                    )
                    val userProfile = withContext(Dispatchers.IO) {
                        privateAuthService.getMe()
                    }

                    // Guardar usuario en SharedPreferences para HomeActivity
                    val prefs = getSharedPreferences("user_session", Context.MODE_PRIVATE)
                    prefs.edit().apply {
                        putInt("id", userProfile.id)
                        putString("name", userProfile.name)
                        putString("email", userProfile.email)
                        putString("phone", userProfile.phone ?: "")
                        putString("shipping_address", userProfile.shippingAddress ?: "")
                        putString("role", userProfile.role ?: "")
                        putString("status", userProfile.status ?: "")
                        putString("lastname", userProfile.lastname ?: "")
                        putString("created_at", userProfile.createdAt?.toString() ?: "")
                        apply()
                    }

                    tokenManager.saveAuth(
                        token = authToken,
                        userName = userProfile.name,
                        userEmail = userProfile.email
                    )

                    Toast.makeText(this@MainActivity, "¡Bienvenido, ${userProfile.name}!", Toast.LENGTH_SHORT).show()
                    goToHome()

                } catch (e: Exception) {
                    Log.e("MainActivity", "Login o GetProfile error", e)
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    tokenManager.clear()
                } finally {
                    binding.progress.visibility = View.GONE
                    binding.btnLogin.isEnabled = true
                }
            }
        }

        // --- Link a pantalla de registro ---
        binding.tvSignUpLink.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}