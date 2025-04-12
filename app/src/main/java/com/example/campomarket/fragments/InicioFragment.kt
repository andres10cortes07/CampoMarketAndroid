package com.example.campomarket.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.activities.AuthActivity

class InicioFragment : Fragment() {

    private lateinit var iconoLogin : ImageView
    private lateinit var iconoCarrito: ImageView
    private lateinit var iconoIngresos: ImageView
    private lateinit var opcionClientes : LinearLayout
    private lateinit var opcionVendedores : LinearLayout
    private lateinit var opcionMas : LinearLayout
    private lateinit var opcionMisProductos : LinearLayout
    private lateinit var iconoSalir : ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inicio, container, false)

        iconoLogin = view.findViewById(R.id.iconoEntrar)
        // Accede al ImageView del header directamente
        iconoCarrito = view.findViewById(R.id.iconoCarrito)
        iconoIngresos = view.findViewById(R.id.iconoIngresos)
        iconoSalir = view.findViewById(R.id.iconoSalir)
        opcionClientes = view.findViewById(R.id.cardOpcionClientes)
        opcionMas = view.findViewById(R.id.cardOpcionMas)
        opcionVendedores = view.findViewById(R.id.cardOpcionVendedores)
        opcionMisProductos = view.findViewById(R.id.cardOpcionMisProductos)

        // Esconder a los iconos y opciones no necesarios
        iconoCarrito.visibility = View.GONE
        iconoIngresos.visibility = View.GONE
        iconoSalir.visibility = View.GONE
        opcionClientes.visibility = View.GONE
        opcionVendedores.visibility = View.GONE
        opcionMas.visibility = View.GONE
        opcionMisProductos.visibility = View.GONE

        iconoLogin.setOnClickListener {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }
}