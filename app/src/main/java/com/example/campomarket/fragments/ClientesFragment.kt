package com.example.campomarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.campomarket.R
import com.example.campomarket.adapters.UsuariosAdapter
import com.example.campomarket.data.model.Usuario
import com.example.campomarket.data.storage.UsuarioManager
import com.example.campomarket.util.NavigationUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ClientesFragment : Fragment() {

    private lateinit var btnCrearComprador: MaterialButton
    private lateinit var recyclerCompradores: RecyclerView
    private lateinit var adapter: UsuariosAdapter
    private val compradoresList = mutableListOf<Usuario>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_clientes, container, false)

        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        btnCrearComprador = view.findViewById(R.id.btnCrearComprador)
        recyclerCompradores = view.findViewById(R.id.recyclerCompradores)

        // Inicializar el adapter con funciones para editar y eliminar
        adapter = UsuariosAdapter(compradoresList,
            onEditarClick = { comprador -> irAEditarComprador(comprador) },
            onEliminarClick = { comprador -> confirmarEliminar(comprador) })

        recyclerCompradores.adapter = adapter

        btnCrearComprador.setOnClickListener {
            val bundle = Bundle().apply {
                putString("fragment_origen", "ClientesFragment")
            }
            findNavController().navigate(R.id.crearUsuario, bundle)
        }

        cargarCompradores()

        return view
    }

    private fun cargarCompradores() {
        // Obtener usuarios y filtrar solo compradores
        val usuarios = UsuarioManager.obtenerUsuarios(requireContext())
        val compradores = usuarios.filter { it.rol == "comprador" }
        compradoresList.clear()
        compradoresList.addAll(compradores)
        adapter.notifyDataSetChanged()
    }

    private fun irAEditarComprador(comprador: Usuario) {
        val bundle = Bundle().apply {
            putString("nombres", comprador.nombres)
            putString("apellidos", comprador.apellidos)
            putString("tipoDocumento", comprador.tipoDocumento)
            putString("numeroIdentificacion", comprador.numeroIdentificacion)
            putString("correo", comprador.correo)
            putString("celular", comprador.celular)
            putString("departamento", comprador.departamento)
            putString("ciudad", comprador.ciudad)
            putString("direccion", comprador.direccion)
            putString("rol", comprador.rol)
            putString("fragment_origen", "ClientesFragment")
        }
         findNavController().navigate(R.id.editarUsuario, bundle)
    }

    private fun confirmarEliminar(comprador: Usuario) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar Usuario")
            .setMessage("¿Está seguro que desea eliminar al comprador ${comprador.nombres} ${comprador.apellidos}?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarComprador(comprador)
            }
            .show()
    }

    private fun eliminarComprador(comprador: Usuario) {
        val usuarios = UsuarioManager.obtenerUsuarios(requireContext())
        val nuevosUsuarios = usuarios.filter { it.correo != comprador.correo }
        UsuarioManager.guardarUsuarios(requireContext(), nuevosUsuarios)
        cargarCompradores()
    }
}
