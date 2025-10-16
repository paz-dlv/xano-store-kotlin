package com.miapp.xanostorekotlin.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.miapp.xanostorekotlin.R
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
                        Log.e("SignUp", "Response body: ${response.body()}")
                        Log.e("SignUp", "Response error: ${response.errorBody()?.string()}")

                        if (response.isSuccessful && response.body() != null) {
                            Toast.makeText(
                                this@SignUpActivity,
                                "Usuario registrado exitosamente. Ahora puedes iniciar sesión.",
                                Toast.LENGTH_LONG
                            ).show()
                            // Navega a LoginActivity (MainActivity)
                            val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
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
                            "Error de red: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("SignUp", "Failure: ${t.message}")
                    }
                })
            }
        }
    }

    private fun parseErrorMessage(errorBody: String?): String {
        if (errorBody.isNullOrEmpty()) {
            return "Error al registrar usuario."
        }
        return try {
            val json = JSONObject(errorBody)
            if (json.has("message")) {
                json.getString("message")
            } else {
                "Error al registrar usuario."
            }
        } catch (e: Exception) {
            "Error al registrar usuario."
        }
    }

    private fun validateFields(name: String, email: String, password: String, address: String, phone: String): Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email no válido.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}