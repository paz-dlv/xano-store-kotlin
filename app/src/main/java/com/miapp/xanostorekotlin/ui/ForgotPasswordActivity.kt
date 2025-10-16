package com.miapp.xanostorekotlin.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.miapp.xanostorekotlin.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRecoverPassword.setOnClickListener {
            val email = binding.etEmail.text?.toString()?.trim().orEmpty()

            if (email.isBlank()) {
                Toast.makeText(this, "Ingresa tu email para recuperar la contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Aquí puedes llamar a tu API para recuperar la contraseña.
            binding.progressBar.visibility = View.VISIBLE

            // Simulación de éxito
            binding.progressBar.postDelayed({
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Si el email existe, recibirás instrucciones para recuperar tu contraseña.", Toast.LENGTH_LONG).show()
            }, 1800)
        }
    }
}