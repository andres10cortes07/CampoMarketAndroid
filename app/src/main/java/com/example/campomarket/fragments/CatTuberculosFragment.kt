package com.example.campomarket.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campomarket.R
import com.example.campomarket.adapters.ProductosAdapter
import com.example.campomarket.adapters.ProductosAdapterCompra
import com.example.campomarket.data.storage.ProductoManager
import com.example.campomarket.data.storage.CarritoManager
import com.example.campomarket.util.NavigationUtil
import com.google.gson.Gson

class CatTuberculosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterAdmin: ProductosAdapter
    private lateinit var adapterCompra: ProductosAdapterCompra

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rolUsuario: String
    private lateinit var emailUsuarioLogueado: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cat_tuberculos, container, false)
        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        rolUsuario = obtenerRolUsuario()
        emailUsuarioLogueado = obtenerEmailUsuarioLogueado() // Obtener el email del usuario logueado

        recyclerView = view.findViewById(R.id.recyclerProductosPorCategoria)

        if (rolUsuario == "ADMINISTRADOR") {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            adapterAdmin = ProductosAdapter(
                productos = mutableListOf(),
                onEditarClick = { producto ->
                    val bundle = Bundle().apply {
                        putString("id", producto.id)
                        putString("nombre", producto.nombre)
                        putString("categoria", producto.categoria)
                        putInt("precioLibra", producto.precioLibra)
                        putInt("disponibilidad", producto.disponibilidad)
                        putString("imagenBase64", producto.imagenBase64)
                        putString("vendedorEmail", producto.vendedorEmail)
                        putString("fragment_origen", "ProductosFragment")
                    }
                    findNavController().navigate(R.id.editarProducto, bundle)
                },
                onEliminarClick = { producto ->
                    val eliminado = ProductoManager.eliminarProducto(requireContext(), producto.id)
                    if (eliminado) {
                        cargarProductos()
                    } else {
                        Log.e("AllProductosFragment", "Error al eliminar producto con ID: ${producto.id}")
                    }
                },
                rolUsuario = rolUsuario
            )
            recyclerView.adapter = adapterAdmin
        } else { // COMPRADOR O VENDEDOR
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            adapterCompra = ProductosAdapterCompra(
                productos = mutableListOf(),
                onAgregarAlCarritoClick = { producto ->
                    // Pasamos el email del usuario logueado al CarritoManager
                    CarritoManager.agregarProductoAlCarrito(requireContext(), emailUsuarioLogueado, producto)
                    Toast.makeText(requireContext(), "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
                }
            )
            recyclerView.adapter = adapterCompra
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        cargarProductos()
    }

    private fun cargarProductos() {
        val productos = ProductoManager.obtenerPorCategoria(requireContext(), "Tuberculos")
        if (rolUsuario == "ADMINISTRADOR") {
            adapterAdmin.actualizarLista(productos)
        } else {
            adapterCompra.actualizarLista(productos)
        }
    }

    private fun obtenerRolUsuario(): String {
        val usuarioJson = sharedPreferences.getString("usuarioLogueado", null)
        return if (usuarioJson != null) {
            val gson = Gson()
            val usuario = gson.fromJson(usuarioJson, com.example.campomarket.data.model.Usuario::class.java)
            usuario.rol?.trim()?.uppercase() ?: ""
        } else ""
    }

    private fun obtenerEmailUsuarioLogueado(): String {
        val usuarioJson = sharedPreferences.getString("usuarioLogueado", null)
        return if (usuarioJson != null) {
            val gson = Gson()
            val usuario = gson.fromJson(usuarioJson, com.example.campomarket.data.model.Usuario::class.java)
            usuario.correo ?: ""
        } else ""
    }

    private fun obtenerEmailVendedor(): String {
        val usuarioJson = sharedPreferences.getString("usuarioLogueado", null)
        return if (usuarioJson != null) {
            val gson = com.google.gson.Gson()
            val usuario = gson.fromJson(usuarioJson, com.example.campomarket.data.model.Usuario::class.java)
            usuario.correo ?: ""
        } else ""
    }
}