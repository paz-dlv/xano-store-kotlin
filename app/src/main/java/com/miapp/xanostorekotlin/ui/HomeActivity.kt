package com.miapp.xanostorekotlin.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.miapp.xanostorekotlin.api.TokenManager
import com.miapp.xanostorekotlin.databinding.ActivityHomeBinding
import com.miapp.xanostorekotlin.ui.fragments.AddProductFragment
import com.miapp.xanostorekotlin.ui.fragments.ProductsFragment
import com.miapp.xanostorekotlin.ui.fragments.ProfileFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenManager = TokenManager(this)

        // Validación de usuario
        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val userName = prefs.getString("name", null)
        if (userName.isNullOrBlank()) {
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
            return
        }

        binding.tvWelcome.text = "Bienvenido $userName"

        // Cargar fragmento inicial
        replaceFragment(ProductsFragment())

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                com.miapp.xanostorekotlin.R.id.nav_profile -> replaceFragment(ProfileFragment())
                com.miapp.xanostorekotlin.R.id.nav_products -> replaceFragment(ProductsFragment())
                com.miapp.xanostorekotlin.R.id.nav_add -> replaceFragment(AddProductFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}