package com.example.campomarket.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.activities.AuthActivity
import com.example.campomarket.util.AdminNavigationUtil

class MasOpcionesFragmentAdmin : Fragment() {

    private lateinit var opcionSalir : LinearLayout
    private lateinit var opcionTiendas : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mas_opciones_admin, container, false)

        AdminNavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        opcionSalir = view.findViewById(R.id.opcionCerrarSesion)
        opcionSalir.setOnClickListener{
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        opcionTiendas = view.findViewById(R.id.opcionTiendas)
        opcionTiendas.setOnClickListener{
            findNavController().navigate(R.id.tiendasFragment)
        }

        return view
    }
}