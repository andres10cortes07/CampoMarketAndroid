package com.example.campomarket.adapters

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campomarket.R
import com.example.campomarket.data.model.Producto
import com.google.android.material.button.MaterialButton

class ProductosAdapter(
    private val productos: MutableList<Producto>,
    private val onEditarClick: (Producto) -> Unit,
    private val onEliminarClick: (Producto) -> Unit,
    private val rolUsuario: String
) : RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProducto: ImageView = view.findViewById(R.id.imgProducto)
        val txtNombre: TextView = view.findViewById(R.id.txtNombreProducto)
        val txtPrecioLibra: TextView = view.findViewById(R.id.txtPrecioLibra)
        val txtDisponibilidad: TextView = view.findViewById(R.id.txtDisponibilidad)
        val txtCategoria: TextView = view.findViewById(R.id.txtCategoria)
        val btnEditar: MaterialButton = view.findViewById(R.id.btnEditarProducto)
        val btnEliminar: MaterialButton = view.findViewById(R.id.btnEliminarProducto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mi_producto_vendedor, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        holder.txtNombre.text = producto.nombre
        holder.txtPrecioLibra.text = "$${producto.precioLibra}"
        holder.txtDisponibilidad.text = "${producto.disponibilidad} libras disponibles"
        holder.txtCategoria.text = producto.categoria

        // Convertir imagen base64 a bitmap
        val imageBytes = Base64.decode(producto.imagenBase64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.imgProducto.setImageBitmap(bitmap)

        holder.btnEditar.setOnClickListener { onEditarClick(producto) }
        holder.btnEliminar.setOnClickListener { onEliminarClick(producto) }
    }

    override fun getItemCount(): Int = productos.size

    fun actualizarLista(nuevaLista: List<Producto>) {
        productos.clear()
        productos.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}