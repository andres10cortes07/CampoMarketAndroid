package com.example.campomarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.fragment.app.Fragment
import com.example.campomarket.R

class MapaFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    private var nombre: String = "Ubicación"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Leer los argumentos enviados desde TiendasFragment
        arguments?.let {
            latitud = it.getDouble("latitud", 0.0)
            longitud = it.getDouble("longitud", 0.0)
            nombre = it.getString("nombre", "Ubicación")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mapa, container, false)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Usar los datos recibidos para mostrar el marcador
        val ubicacion = LatLng(latitud, longitud)
        googleMap.addMarker(MarkerOptions().position(ubicacion).title(nombre))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f))
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    fun actualizarUbicacion(lat: Double, lng: Double, nombre: String) {
        if (::googleMap.isInitialized) {
            val ubicacion = LatLng(lat, lng)
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(ubicacion).title(nombre))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f))
        }
    }
}
