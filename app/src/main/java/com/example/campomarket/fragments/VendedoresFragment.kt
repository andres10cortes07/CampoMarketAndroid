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

class VendedoresFragment : Fragment() {

    private lateinit var btnCrearVendedor: MaterialButton
    private lateinit var recyclerVendedores: RecyclerView
    private lateinit var adapter: UsuariosAdapter
    private val vendedoresList = mutableListOf<Usuario>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vendedores, container, false)

        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        btnCrearVendedor = view.findViewById(R.id.btnCrearVendedor)
        recyclerVendedores = view.findViewById(R.id.recyclerVendedores)

        // Inicializar el adapter con funciones para editar y eliminar
        adapter = UsuariosAdapter(vendedoresList,
            onEditarClick = { vendedor -> irAEditarVendedor(vendedor) },
            onEliminarClick = { vendedor -> confirmarEliminar(vendedor) })

        recyclerVendedores.adapter = adapter

        btnCrearVendedor.setOnClickListener {
            val bundle = Bundle().apply {
                putString("fragment_origen", "VendedoresFragment")
            }
            findNavController().navigate(R.id.crearUsuario, bundle)
        }

        cargarVendedores()

        return view
    }

    private fun cargarVendedores() {
        // Obtener usuarios y filtrar solo vendedores
        val usuarios = UsuarioManager.obtenerUsuarios(requireContext())
        val vendedores = usuarios.filter { it.rol == "vendedor" }
        vendedoresList.clear()
        vendedoresList.addAll(vendedores)
        adapter.notifyDataSetChanged()
    }

    private fun irAEditarVendedor(vendedor: Usuario) {
        val bundle = Bundle().apply {
            putString("nombres", vendedor.nombres)
            putString("apellidos", vendedor.apellidos)
            putString("tipoDocumento", vendedor.tipoDocumento)
            putString("numeroIdentificacion", vendedor.numeroIdentificacion)
            putString("correo", vendedor.correo)
            putString("celular", vendedor.celular)
            putString("departamento", vendedor.departamento)
            putString("ciudad", vendedor.ciudad)
            putString("direccion", vendedor.direccion)
            putString("rol", vendedor.rol)
            putString("fragment_origen", "VendedoresFragment")
        }
         findNavController().navigate(R.id.editarUsuario, bundle)
    }

    private fun confirmarEliminar(vendedor: Usuario) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar Usuario")
            .setMessage("¿Está seguro que desea eliminar al vendedor ${vendedor.nombres} ${vendedor.apellidos}?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarVendedor(vendedor)
            }
            .show()
    }

    private fun eliminarVendedor(vendedor: Usuario) {
        val usuarios = UsuarioManager.obtenerUsuarios(requireContext())
        val nuevosUsuarios = usuarios.filter { it.correo != vendedor.correo }
        UsuarioManager.guardarUsuarios(requireContext(), nuevosUsuarios)
        cargarVendedores()
    }
}
