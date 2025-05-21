package com.example.campomarket.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.data.storage.CompraManager
import com.example.campomarket.util.NavigationUtil
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class ComprasFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var emailUsuarioLogueado: String

    private lateinit var contenedorHistoricoCompras: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_compras, container, false)
        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        emailUsuarioLogueado = obtenerEmailUsuarioLogueado()

        // Inicializa el contenedor principal donde se añadirán las tarjetas de compra
        contenedorHistoricoCompras = view.findViewById(R.id.contenedorHistoricoCompras)

        return view
    }

    override fun onResume() {
        super.onResume()
        cargarHistorialCompras()
    }

    private fun cargarHistorialCompras() {
        // Limpia las vistas existentes para evitar duplicados al reanudar el fragmento
        contenedorHistoricoCompras.removeAllViews()

        val comprasUsuario = CompraManager.obtenerComprasPorUsuario(requireContext(), emailUsuarioLogueado)
        // Ordena las compras de la más reciente a la más antigua
        val comprasOrdenadas = comprasUsuario.sortedByDescending { it.fechaCompra }

        if (comprasOrdenadas.isEmpty()) {
            // Muestra un mensaje si el historial de compras está vacío
            val noComprasText = TextView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 50, 0, 0) // Margen superior para separarlo del título
                }
                text = "Aún no tienes compras realizadas."
                gravity = android.view.Gravity.CENTER_HORIZONTAL
                textSize = 16f
                setTextColor(resources.getColor(R.color.gris, null))
            }
            contenedorHistoricoCompras.addView(noComprasText)
        } else {
            // Itera sobre cada compra y añade su vista (tarjeta) dinámicamente
            for (compra in comprasOrdenadas) {
                val cardCompraView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_compra_card, contenedorHistoricoCompras, false) // Asegúrate de que R.layout.item_compra_card apunte a tu XML de tarjeta de compra

                // Encuentra las vistas dentro de la tarjeta de compra inflada
                val tvFecha = cardCompraView.findViewById<TextView>(R.id.tvFecha)
                val tvTotal = cardCompraView.findViewById<TextView>(R.id.tvTotal)
                val contenedorProductosCompra = cardCompraView.findViewById<LinearLayout>(R.id.contenedorProductosCompra)

                // Rellena la tarjeta de compra con los datos
                val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                tvFecha.text = "Fecha: ${formatoFecha.format(compra.fechaCompra)}"
                tvTotal.text = "Total: $${compra.total}"

                // Limpia el contenedor de productos dentro de esta tarjeta antes de añadir nuevos productos
                contenedorProductosCompra.removeAllViews()

                // Itera sobre los productos en esta compra y añade sus vistas dinámicamente
                for (item in compra.items) {
                    val productoItemView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.item_producto_en_compra_simple, contenedorProductosCompra, false)

                    val tvNombreProducto = productoItemView.findViewById<TextView>(R.id.tvNombreProductoHistorialSimple)
                    val tvCantidadPrecio = productoItemView.findViewById<TextView>(R.id.tvCantidadPrecioHistorialSimple)
                    val tvPrecioTotalProducto = productoItemView.findViewById<TextView>(R.id.tvPrecioTotalProductoHistorialSimple)

                    tvNombreProducto.text = item.producto.nombre
                    tvCantidadPrecio.text = "${item.cantidad} x $${item.producto.precioLibra}/libra"
                    tvPrecioTotalProducto.text = "$${item.producto.precioLibra * item.cantidad}"

                    contenedorProductosCompra.addView(productoItemView)
                }
                // Finalmente, añade la tarjeta de compra completamente poblada al contenedor principal del historial
                contenedorHistoricoCompras.addView(cardCompraView)
            }
        }
    }

    private fun obtenerEmailUsuarioLogueado(): String {
        val usuarioJson = sharedPreferences.getString("usuarioLogueado", null)
        return if (usuarioJson != null) {
            val gson = Gson()
            val usuario = gson.fromJson(usuarioJson, com.example.campomarket.data.model.Usuario::class.java)
            usuario.correo ?: ""
        } else ""
    }
}