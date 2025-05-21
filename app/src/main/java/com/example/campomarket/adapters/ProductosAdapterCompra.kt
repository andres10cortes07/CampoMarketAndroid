package com.example.campomarket.adapters

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campomarket.R
import com.example.campomarket.data.model.Producto
import com.google.android.material.button.MaterialButton

class ProductosAdapterCompra(
    private val productos: MutableList<Producto>,
    private val onAgregarAlCarritoClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductosAdapterCompra.ProductoViewHolder>() {

    inner class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProducto: ImageView = view.findViewById(R.id.imgProducto)
        val txtNombre: TextView = view.findViewById(R.id.txtNombreProducto)
        val txtPrecioLibra: TextView = view.findViewById(R.id.txtPrecioLibra)
        val btnAgregarAlCarrito: MaterialButton = view.findViewById(R.id.btnAgregarAlCarrito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto_compra, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        holder.txtNombre.text = producto.nombre
        holder.txtPrecioLibra.text = "$${producto.precioLibra}"

        val imageBytes = Base64.decode(producto.imagenBase64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.imgProducto.setImageBitmap(bitmap)

        holder.btnAgregarAlCarrito.setOnClickListener {
            onAgregarAlCarritoClick(producto)
        }
    }

    override fun getItemCount(): Int = productos.size

    fun actualizarLista(nuevaLista: List<Producto>) {
        productos.clear()
        productos.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}