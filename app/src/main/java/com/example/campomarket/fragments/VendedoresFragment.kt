package com.example.campomarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.util.NavigationUtil
import com.google.android.material.button.MaterialButton

class VendedoresFragment : Fragment () {

    private lateinit var btnCrearVendedor : MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vendedores, container, false)
        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        // Guardar el nombre del fragment actual para manejarlo en el crear usuario
        val bundle = Bundle()
        bundle.putString("fragment_origen", "VendedoresFragment")

        btnCrearVendedor = view.findViewById(R.id.btnCrearVendedor)
        btnCrearVendedor.setOnClickListener{
            findNavController().navigate(R.id.crearUsuario, bundle)
        }

        return view
    }

}