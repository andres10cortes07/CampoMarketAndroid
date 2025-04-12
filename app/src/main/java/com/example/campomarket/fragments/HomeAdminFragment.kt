package com.example.campomarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R

class HomeAdminFragment : Fragment() {

    private lateinit var iconoCarrito : ImageView
    private lateinit var iconoEntrar: ImageView
    private lateinit var iconoIngresos: ImageView
    private lateinit var opcionTiendas : LinearLayout
    private lateinit var opcionMisProductos : LinearLayout
    private lateinit var opcionProductos : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_admin, container, false)

        // Accede al ImageView del header directamente
        opcionProductos = view.findViewById(R.id.cardOpcionProductos)

        opcionProductos.setOnClickListener {
            findNavController().navigate(R.id.categoriasProductosFragment)
        }

        return view
    }
}