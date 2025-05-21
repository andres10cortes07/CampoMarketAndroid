package com.example.campomarket.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campomarket.R
import com.example.campomarket.adapters.CarritoAdapter
import com.example.campomarket.data.storage.CarritoManager
import com.example.campomarket.data.storage.CompraManager // Importar CompraManager
import com.example.campomarket.util.NavigationUtil
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.example.campomarket.data.model.Usuario // Importar Usuario

class CarritoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carritoAdapter: CarritoAdapter
    private lateinit var precioTotalCarrito: TextView
    private lateinit var btnComprarCarrito: MaterialButton

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var emailUsuarioLogueado: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_carrito, container, false)
        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        emailUsuarioLogueado = obtenerEmailUsuarioLogueado()

        recyclerView = view.findViewById(R.id.recyclerProductosEnCarrito)
        precioTotalCarrito = view.findViewById(R.id.precioTotalCarrito)
        btnComprarCarrito = view.findViewById(R.id.btnComprarCarrito)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        carritoAdapter = CarritoAdapter(
            onQuantityChanged = { itemCarrito, newQuantity ->
                CarritoManager.actualizarCantidadProducto(requireContext(), emailUsuarioLogueado, itemCarrito.producto.id, newQuantity)
                actualizarTotalCarrito()
            },
            onRemoveClick = { itemCarrito ->
                CarritoManager.eliminarProductoDelCarrito(requireContext(), emailUsuarioLogueado, itemCarrito.producto.id)
                cargarCarrito()
            }
        )
        recyclerView.adapter = carritoAdapter

        btnComprarCarrito.setOnClickListener {
            val itemsEnCarrito = CarritoManager.obtenerCarrito(requireContext(), emailUsuarioLogueado)
            if (itemsEnCarrito.isNotEmpty()) {
                CompraManager.registrarCompra(requireContext(), emailUsuarioLogueado, itemsEnCarrito)

                // Limpiar el carrito después de la compra
                CarritoManager.limpiarCarrito(requireContext(), emailUsuarioLogueado)
                cargarCarrito() // Vuelve a cargar el carrito (ahora vacío)
                actualizarTotalCarrito() // Actualiza el total a 0

                Toast.makeText(requireContext(), "¡Compra realizada con éxito!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "El carrito está vacío.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        cargarCarrito()
    }

    private fun cargarCarrito() {
        val carritoItems = CarritoManager.obtenerCarrito(requireContext(), emailUsuarioLogueado)
        carritoAdapter.actualizarLista(carritoItems)
        actualizarTotalCarrito()
    }

    private fun actualizarTotalCarrito() {
        val carritoItems = CarritoManager.obtenerCarrito(requireContext(), emailUsuarioLogueado)
        var total = 0
        for (item in carritoItems) {
            total += item.producto.precioLibra * item.cantidad
        }
        precioTotalCarrito.text = "$${total}"
    }

    private fun obtenerEmailUsuarioLogueado(): String {
        val usuarioJson = sharedPreferences.getString("usuarioLogueado", null)
        return if (usuarioJson != null) {
            val gson = Gson()
            val usuario = gson.fromJson(usuarioJson, Usuario::class.java)
            usuario.correo ?: ""
        } else ""
    }
}