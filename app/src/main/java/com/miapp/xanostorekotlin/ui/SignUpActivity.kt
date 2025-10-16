package com.miapp.xanostorekotlin.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.miapp.xanostorekotlin.R
import com.miapp.xanostorekotlin.api.ApiConfig
import com.miapp.xanostorekotlin.api.RetrofitClient
import com.miapp.xanostorekotlin.model.RegisterUserRequest
import com.miapp.xanostorekotlin.model.LoginRequest
import com.miapp.xanostorekotlin.model.User
import com.miapp.xanostorekotlin.model.AuthResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val nameEditText = findViewById<EditText>(R.id.etName)
        val emailEditText = findViewById<EditText>(R.id.etEmail)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val addressEditText = findViewById<EditText>(R.id.etShippingAddress)
        val phoneEditText = findViewById<EditText>(R.id.etPhone)
        val signUpButton = findViewById<Button>(R.id.btnSignUp)

        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val address = addressEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()

            if (validateFields(name, email, password, address, phone)) {
                val request = RegisterUserRequest(
                    name = name,
                    email = email,
                    password = password,
                    shipping_address = address,
                    phone = phone
                )

                val authService = RetrofitClient.createAuthService(
                    context = this,
                    baseUrl = ApiConfig.authBaseUrl
                )
                authService.signUp(request).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {

                        // --- LOGS PARA DEPURAR ---
                        Log.e("SignUp", "Response body: ${response.body()}")
                        Log.e("SignUp", "Response error: ${response.errorBody()?.string()}")

                        if (response.isSuccessful && response.body() != null) {
                            val usuario = response.body()!!

                            // Guardar el usuario en SharedPreferences
                            val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
                            prefs.edit().apply {
                                putInt("id", usuario.id)
                                putString("name", usuario.name)
                                putString("email", usuario.email)
                                putString("phone", usuario.phone)
                                putString("shipping_address", usuario.shippingAddress ?: "")
                                putString("role", usuario.role ?: "")
                                putString("status", usuario.status ?: "")
                                putString("lastname", usuario.lastname ?: "")
                                putString("created_at", usuario.createdAt ?: "")
                                apply()
                            }

                            Log.d("SignUp", "Usuario recibido: id=${usuario.id}, name=${usuario.name}, email=${usuario.email}")

                            // LOGIN AUTOMÁTICO TRAS REGISTRO (usando coroutine y suspend fun)
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val loginRequest = LoginRequest(email = usuario.email, password = password)
                                    val loginResponse: AuthResponse = authService.login(loginRequest)

                                    // Guarda el token en SharedPreferences
                                    withContext(Dispatchers.Main) {
                                        val prefsSession = getSharedPreferences("session", MODE_PRIVATE)
                                        prefsSession.edit().putString("jwt_token", loginResponse.authToken).apply()

                                        Toast.makeText(
                                            this@SignUpActivity,
                                            getString(R.string.register_success) + " " + usuario.name,
                                            Toast.LENGTH_LONG
                                        ).show()

                                        // Navegar automáticamente a HomeActivity y cerrar esta
                                        val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            this@SignUpActivity,
                                            getString(R.string.error_login),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            val errorMessage = parseErrorMessage(errorBody)
                            Toast.makeText(
                                this@SignUpActivity,
                                errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(
                            this@SignUpActivity,
                            getString(R.string.error_network),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }

    // Extrae el mensaje de error para mostrarlo al usuario
    private fun parseErrorMessage(errorBody: String?): String {
        if (errorBody.isNullOrEmpty()) {
            return getString(R.string.error_registration)
        }
        return try {
            val json = JSONObject(errorBody)
            when {
                json.has("message") && json.getString("message").contains("email", ignoreCase = true) &&
                        json.getString("message").contains("exists", ignoreCase = true) ->
                    getString(R.string.error_email_exists)
                json.has("message") -> json.getString("message")
                else -> getString(R.string.error_registration)
            }
        } catch (e: Exception) {
            if (errorBody.contains("email", ignoreCase = true) &&
                errorBody.contains("exist", ignoreCase = true)) {
                return getString(R.string.error_email_exists)
            }
            getString(R.string.error_registration)
        }
    }

    private fun validateFields(name: String, email: String, password: String, address: String, phone: String): Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_fill_fields), Toast.LENGTH_SHORT).show()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, getString(R.string.error_password_short), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}