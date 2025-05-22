package com.example.campomarket.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campomarket.R
import com.example.campomarket.activities.AuthActivity
import com.example.campomarket.adapters.ProductosAdapterCompra
import com.example.campomarket.data.model.Producto
import com.example.campomarket.data.model.Usuario
import com.example.campomarket.data.storage.CarritoManager
import com.example.campomarket.data.storage.ProductoManager
import com.example.campomarket.util.Constantes
import com.example.campomarket.util.NavigationUtil
import com.google.gson.Gson

class InicioFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterCompra: ProductosAdapterCompra
    private lateinit var iconoLogin: ImageView

    private lateinit var sharedPreferences: SharedPreferences
    private var emailUsuarioLogueado: String? = null // Hacerlo nullable para cuando no hay sesión

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inicio, container, false)

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        recyclerView = view.findViewById(R.id.recyclerProductosPorCategoria)
        iconoLogin = view.findViewById(R.id.iconoEntrar)

        iconoLogin.setOnClickListener {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapterCompra = ProductosAdapterCompra(
            productos = mutableListOf(),
            onAgregarAlCarritoClick = { producto ->
                // Obtener el email del usuario logueado en el momento del clic
                emailUsuarioLogueado = obtenerEmailUsuarioLogueado()

                if (emailUsuarioLogueado.isNullOrEmpty()) {
                    // Si no hay usuario logueado, guardar el producto pendiente y redirigir al login
                    guardarProductoPendiente(producto)
                    Toast.makeText(requireContext(), "Inicia sesión para agregar productos al carrito.", Toast.LENGTH_LONG).show()
                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    startActivity(intent)
                } else {
                    // Si hay usuario logueado, agregar directamente al carrito
                    CarritoManager.agregarProductoAlCarrito(requireContext(), emailUsuarioLogueado!!, producto)
                    Toast.makeText(requireContext(), "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
                }
            }
        )
        recyclerView.adapter = adapterCompra

        return view
    }

    override fun onResume() {
        super.onResume()
        emailUsuarioLogueado = obtenerEmailUsuarioLogueado()
        cargarProductos()
        verificarProductoPendiente() // Verificar si hay un producto pendiente después de regresar del login
    }

    private fun cargarProductos() {
        val productos = ProductoManager.obtenerProductos(requireContext())
        adapterCompra.actualizarLista(productos)
    }

    private fun obtenerEmailUsuarioLogueado(): String? {
        val usuarioJson = sharedPreferences.getString("usuarioLogueado", null)
        return if (usuarioJson != null) {
            val gson = Gson()
            val usuario = gson.fromJson(usuarioJson, Usuario::class.java)
            usuario.correo?.trim() // Retorna el email o null
        } else null
    }

    private fun guardarProductoPendiente(producto: Producto) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val productoJson = gson.toJson(producto)
        editor.putString(Constantes.KEY_PRODUCTO_PENDIENTE_CARRITO, productoJson)
        editor.apply()
    }

    private fun verificarProductoPendiente() {
        val productoJson = sharedPreferences.getString(Constantes.KEY_PRODUCTO_PENDIENTE_CARRITO, null)

        if (productoJson != null && !emailUsuarioLogueado.isNullOrEmpty()) {
            val gson = Gson()
            val productoPendiente = gson.fromJson(productoJson, Producto::class.java)

            // Agregar el producto al carrito del usuario recién logueado
            CarritoManager.agregarProductoAlCarrito(requireContext(), emailUsuarioLogueado!!, productoPendiente)
            Toast.makeText(requireContext(), "${productoPendiente.nombre} agregado al carrito después de iniciar sesión.", Toast.LENGTH_LONG).show()

            // Limpiar el producto pendiente para que no se agregue de nuevo
            sharedPreferences.edit().remove(Constantes.KEY_PRODUCTO_PENDIENTE_CARRITO).apply()
        }
    }
}