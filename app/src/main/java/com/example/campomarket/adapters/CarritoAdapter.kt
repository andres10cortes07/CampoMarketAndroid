package com.example.campomarket.adapters

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.campomarket.R
import com.example.campomarket.data.model.ItemCarrito

class CarritoAdapter(
    private val onQuantityChanged: (ItemCarrito, Int) -> Unit,
    private val onRemoveClick: (ItemCarrito) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    private val carritoItems: MutableList<ItemCarrito> = mutableListOf()

    inner class CarritoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProducto: ImageView = view.findViewById(R.id.imgProducto)
        val tvNombreProducto: TextView = view.findViewById(R.id.tvNombreProducto)
        val tvPrecio: TextView = view.findViewById(R.id.tvPrecio)
        val tvDisponibilidad: TextView = view.findViewById(R.id.tvDisponibilidad)
        val numDisponibilidad: TextView = view.findViewById(R.id.numDisponibilidad)
        val btnRestar: Button = view.findViewById(R.id.btnRestar)
        val tvCantidad: TextView = view.findViewById(R.id.tvCantidad)
        val btnSumar: Button = view.findViewById(R.id.btnSumar)
        val btnEliminar: ImageButton = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto_en_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val itemCarrito = carritoItems[position]
        val producto = itemCarrito.producto

        holder.tvNombreProducto.text = producto.nombre
        holder.tvPrecio.text = "$${producto.precioLibra * itemCarrito.cantidad}"
        holder.tvCantidad.text = itemCarrito.cantidad.toString()
        holder.numDisponibilidad.text = producto.disponibilidad.toString()

        // Convertir imagen base64 a bitmap
        val imageBytes = Base64.decode(producto.imagenBase64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.imgProducto.setImageBitmap(bitmap)

        holder.btnRestar.setOnClickListener {
            if (itemCarrito.cantidad > 1) {
                itemCarrito.cantidad--
                onQuantityChanged(itemCarrito, itemCarrito.cantidad)
                notifyItemChanged(position)
            }
        }

        holder.btnSumar.setOnClickListener {
            if (itemCarrito.cantidad < producto.disponibilidad) { // Evitar exceder la disponibilidad
                itemCarrito.cantidad++
                onQuantityChanged(itemCarrito, itemCarrito.cantidad)
                notifyItemChanged(position)
            } else {
                Toast.makeText(holder.itemView.context, "Tu petición supera la disponibilidad del producto", Toast.LENGTH_SHORT).show()
            }
        }

        holder.btnEliminar.setOnClickListener {
            onRemoveClick(itemCarrito)
        }
    }

    override fun getItemCount(): Int = carritoItems.size

    fun actualizarLista(nuevaLista: List<ItemCarrito>) {
        carritoItems.clear()
        carritoItems.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}