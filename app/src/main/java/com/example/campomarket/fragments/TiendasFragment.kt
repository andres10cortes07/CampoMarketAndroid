package com.example.campomarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.util.NavigationUtil
import android.widget.LinearLayout
import com.google.android.gms.maps.model.LatLng

class TiendasFragment : Fragment() {

    private val mapaFragment = MapaFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tiendas, container, false)

        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        // Insertamos el mapa
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.contenedorMapa, mapaFragment)
        transaction.commit()

        // Clics en las bodegas
        view.findViewById<LinearLayout>(R.id.bodega1Layout).setOnClickListener {
            mostrarUbicacionEnMapa(4.7110, -74.0721, "Bodega 1") // Bogotá
        }

        view.findViewById<LinearLayout>(R.id.bodega2Layout).setOnClickListener {
            mostrarUbicacionEnMapa(4.6097, -74.0817, "Bodega 2") // Candelaria
        }

        view.findViewById<LinearLayout>(R.id.bodega3Layout).setOnClickListener {
            mostrarUbicacionEnMapa(4.6561, -74.0596, "Bodega 3") // Chapinero
        }

        return view
    }

    private fun mostrarUbicacionEnMapa(lat: Double, lng: Double, nombre: String) {
        mapaFragment.actualizarUbicacion(lat, lng, nombre)
    }

}
