package com.miapp.xanostorekotlin.ui // Define el paquete al que pertenece esta clase.

import android.os.Build // Importa la clase Build para verificar la versión de Android del dispositivo.
import android.os.Bundle // Importa la clase Bundle, usada para pasar datos entre actividades.
import android.view.MenuItem // Importa la clase MenuItem para identificar los botones de la barra de acción.
import androidx.appcompat.app.AppCompatActivity // Importa la clase base para actividades que usan la barra de acción de compatibilidad.
import com.miapp.xanostorekotlin.databinding.ActivityProductDetailBinding // Importa la clase de ViewBinding generada para nuestro layout.
import com.miapp.xanostorekotlin.model.Product // Importa nuestro modelo de datos 'Product'.
import com.miapp.xanostorekotlin.ui.adapter.ImageSliderAdapter // Importa el adaptador que creamos para el carrusel de imágenes.

/**
 * ProductDetailActivity
 * Esta actividad muestra los detalles completos de un solo producto,
 * incluyendo un carrusel de imágenes y un botón para volver atrás.
 */
class ProductDetailActivity : AppCompatActivity() { // La clase hereda de AppCompatActivity.

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- HABILITAR EL BOTÓN DE "VOLVER ATRÁS" ---
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // --- RECUPERAR EL PRODUCTO DEL INTENT ---
        val product = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("PRODUCT_EXTRA", Product::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("PRODUCT_EXTRA") as? Product
        }

        product?.let {
            setupUI(it)
        }
    }

    // Este métodoo configura todos los elementos de la interfaz de usuario con los datos del producto.
    private fun setupUI(product: Product) {
        // --- TÍTULO DE LA ACTIVIDAD ---
        // Establece el título de la barra de acción (ActionBar) con el título del producto.
        title = product.title

        // --- CONFIGURAR LOS TEXTOS ---
        // Asigna el título del producto al TextView correspondiente.
        binding.tvProductName.text = "Título: ${product.title}"
        // Asigna el autor.
        binding.tvProductAuthor.text = "Autor: ${product.author}"
        // Asigna el género.
        binding.tvProductGenre.text = "Género: ${product.genre}"
        // Asigna el precio.
        binding.tvProductPrice.text = "Precio: S/ ${product.price}"
        // Asigna la descripción (puede ser nula).
        binding.tvProductDescription.text = "Descripción: ${product.description ?: "Sin descripción."}"
        // Asigna el stock.
        binding.tvProductStock.text = "Stock: ${product.stock}"

        // --- CONFIGURAR EL CARRUSEL DE IMÁGENES ---
        product.images?.let { imageList ->
            val imageUrls = imageList.mapNotNull { it.url }
            if (imageUrls.isNotEmpty()) {
                val adapter = ImageSliderAdapter(imageUrls)
                binding.imageViewPager.adapter = adapter
            }
        }
    }

    // --- MANEJAR EL CLIC EN EL BOTÓN "VOLVER ATRÁS" ---
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}