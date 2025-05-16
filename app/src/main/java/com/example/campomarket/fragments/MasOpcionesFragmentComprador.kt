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
import com.example.campomarket.util.NavigationUtil

class MasOpcionesFragmentComprador : Fragment() {

    private lateinit var opcionSalir : LinearLayout
    private lateinit var opcionTiendas : LinearLayout
    private lateinit var opcionCompras : LinearLayout
    private lateinit var opcionCambiarClave : LinearLayout
    private lateinit var opcionCuenta : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mas_opciones_comprador, container, false)

        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        opcionSalir = view.findViewById(R.id.opcionCerrarSesion)
        opcionSalir.setOnClickListener{
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        opcionCompras = view.findViewById(R.id.opcionCompras)
        opcionCompras.setOnClickListener{
            findNavController().navigate(R.id.misComprasFragment)
        }

        opcionTiendas = view.findViewById(R.id.opcionTiendas)
        opcionTiendas.setOnClickListener{
            findNavController().navigate(R.id.tiendasFragment)
        }

        opcionCuenta = view.findViewById(R.id.opcionEditarCuenta)
        opcionCuenta.setOnClickListener{
            findNavController().navigate(R.id.editarCuentaFragment)
        }

        opcionCambiarClave = view.findViewById(R.id.opcionCambiarClave)
        opcionCambiarClave.setOnClickListener{
            findNavController().navigate(R.id.cambiarClaveFragment)
        }

        return view
    }
}