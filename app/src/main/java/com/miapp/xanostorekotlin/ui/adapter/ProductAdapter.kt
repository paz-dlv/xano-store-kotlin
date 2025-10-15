package com.miapp.xanostorekotlin.ui.adapter // Paquete del adaptador de RecyclerView

import android.content.Intent
import android.view.LayoutInflater // Import para inflar layouts
import android.view.ViewGroup // Import del contenedor padre en RecyclerView
import androidx.recyclerview.widget.RecyclerView // Import de la clase base RecyclerView
import com.miapp.xanostorekotlin.model.Product // Import del modelo Product (actualizado)
import com.miapp.xanostorekotlin.databinding.ItemProductBinding // Import del ViewBinding del item_product.xml
import coil.load // Extensión de Coil para cargar imágenes en ImageView
import com.miapp.xanostorekotlin.ui.ProductDetailActivity

/**
 * ProductAdapter
 * Adaptador para mostrar productos en un RecyclerView.
 * ACTUALIZADO para usar la nueva estructura del modelo de datos de Product.
 */
class ProductAdapter(private var items: List<Product> = emptyList()) : // Adaptador que recibe lista de productos
    RecyclerView.Adapter<ProductAdapter.VH>() { // Especificamos el ViewHolder interno

    // ViewHolder interno que contiene una referencia al ViewBinding de un item.
    class VH(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val product = items[position]

        // 1. MANEJO DE IMAGENES
        if (!product.images.isNullOrEmpty()) {
            val imageUrl = product.images[0].url
            android.util.Log.d("ProductAdapter", "Cargando imagen desde: $imageUrl")
            holder.binding.imgProduct.load(imageUrl) {
                placeholder(android.R.drawable.ic_menu_gallery)
                error(android.R.drawable.ic_dialog_alert)
            }
        } else {
            holder.binding.imgProduct.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        // 2. MANEJO DEL CLIC
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT_EXTRA", product)
            context.startActivity(intent)
        }

        // 3. ASIGNACIÓN DE TEXTOS con nuevos campos
        // Mostramos el título del producto
        holder.binding.tvTitle.text = product.title

        // Mostramos el autor (opcional, si tu diseño lo permite)
        holder.binding.tvAuthor?.text = product.author // Usa ? si el TextView es opcional

        // Mostramos el género (opcional)
        holder.binding.tvGenre?.text = product.genre

        // Mostramos la descripción
        holder.binding.tvDescription.text = product.description ?: ""

        // Mostramos el precio (formateado)
        holder.binding.tvPrice.text = "Precio: S/ ${product.price}"

        // Mostramos el stock (opcional)
        holder.binding.tvStock?.text = "Stock: ${product.stock}"
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Product>) {
        items = newItems
        notifyDataSetChanged()
    }
}
