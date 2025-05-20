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

class ProductosAdapterCompra(
    private val productos: MutableList<Producto>,
    private val onComprarClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductosAdapterCompra.ProductoViewHolder>() {

    inner class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProducto: ImageView = view.findViewById(R.id.imgProducto)
        val txtNombre: TextView = view.findViewById(R.id.txtNombreProducto)
        val txtPrecioUnidad: TextView = view.findViewById(R.id.txtValorUni)
        val txtPrecioLibra: TextView = view.findViewById(R.id.txtPrecioLibra)
//        val btnComprar: Button = view.findViewById(R.id.btnComprarProducto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto_compra, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        holder.txtNombre.text = producto.nombre
        holder.txtPrecioUnidad.text = "Und a $${producto.precioUnitario}"
        holder.txtPrecioLibra.text = "$${producto.precioLibra}"

        // Convertir imagen base64 a bitmap
        val imageBytes = Base64.decode(producto.imagenBase64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.imgProducto.setImageBitmap(bitmap)

//        holder.btnComprar.setOnClickListener { onComprarClick(producto) }
    }

    override fun getItemCount(): Int = productos.size

    fun actualizarLista(nuevaLista: List<Producto>) {
        productos.clear()
        productos.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}
