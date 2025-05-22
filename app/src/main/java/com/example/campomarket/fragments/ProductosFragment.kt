package com.example.campomarket.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campomarket.R
import com.example.campomarket.adapters.ProductosAdapter
import com.example.campomarket.data.model.Producto
import com.example.campomarket.data.model.Usuario
import com.example.campomarket.data.storage.ProductoManager
import com.example.campomarket.util.Constantes
import com.example.campomarket.util.NavigationUtil
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson

class ProductosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductosAdapter
    private lateinit var btnCrearProducto: MaterialButton
    private val productos = mutableListOf<Producto>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_productos_vendedor, container, false)
        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        recyclerView = view.findViewById(R.id.recyclerMisProductos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val rolUsuario = obtenerRolUsuario()

        adapter = ProductosAdapter(
            productos,
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
                ProductoManager.eliminarProducto(requireContext(), producto.id)
                cargarProductos()
            },
            rolUsuario = rolUsuario
        )


        btnCrearProducto = view.findViewById(R.id.btnCrearProducto)
        btnCrearProducto.setOnClickListener {
            findNavController().navigate(R.id.crearProductoFragment)
        }
        recyclerView.adapter = adapter

        return view
    }

    override fun onResume() {
        super.onResume()
        cargarProductos()
    }

    private fun cargarProductos() {
        val correoUsuario = obtenerCorreoUsuario()
        val productosFiltrados = ProductoManager.obtenerPorVendedor(requireContext(), correoUsuario)
        adapter.actualizarLista(productosFiltrados)
    }

    private fun obtenerCorreoUsuario(): String {
        val prefs = requireContext().getSharedPreferences(Constantes.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val usuarioJson = prefs.getString("usuarioLogueado", null)
        return if (usuarioJson != null) {
            val usuario = Gson().fromJson(usuarioJson, Usuario::class.java)
            usuario.correo ?: ""
        } else {
            ""
        }
    }

    private fun obtenerRolUsuario(): String {
        val prefs = requireContext().getSharedPreferences(Constantes.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val usuarioJson = prefs.getString("usuarioLogueado", null)
        return if (usuarioJson != null) {
            val usuario = Gson().fromJson(usuarioJson, Usuario::class.java)
            usuario.rol ?: ""
        } else {
            ""
        }
    }
}