package com.miapp.xanostorekotlin

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.ads.mediationtestsuite.activities.HomeActivity
import com.miapp.xanostorekotlin.api.RetrofitClient
import com.miapp.xanostorekotlin.model.RegisterUserRequest
import com.miapp.xanostorekotlin.model.User
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

                val authService = RetrofitClient.createAuthService(this)
                authService.signUp(request).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
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
                                putString("created_at", usuario.createdAt?.toString() ?: "")
                                apply()
                            }

                            Toast.makeText(
                                this@SignUpActivity,
                                "¡Registro exitoso! Bienvenido ${usuario.name}",
                                Toast.LENGTH_LONG
                            ).show()

                            // Navegar automáticamente a HomeActivity y cerrar esta
                            val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            // Mejor manejo de mensajes de error
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
                            "Error de red: ${t.message}",
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
            return "Error en el registro. Verifica tus datos."
        }
        return try {
            val json = JSONObject(errorBody)
            when {
                json.has("message") && json.getString("message").contains("email", ignoreCase = true) &&
                        json.getString("message").contains("exists", ignoreCase = true) ->
                    "El correo electrónico ya está registrado."
                json.has("message") -> json.getString("message")
                else -> "Error en el registro. Verifica tus datos."
            }
        } catch (e: Exception) {
            // Si el error no es JSON, usa heurística simple
            if (errorBody.contains("email", ignoreCase = true) &&
                errorBody.contains("exist", ignoreCase = true)) {
                return "El correo electrónico ya está registrado."
            }
            "Error en el registro. Verifica tus datos."
        }
    }

    private fun validateFields(name: String, email: String, password: String, address: String, phone: String): Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}