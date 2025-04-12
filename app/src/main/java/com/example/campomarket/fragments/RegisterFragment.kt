package com.example.campomarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R

class RegisterFragment : Fragment() {

    private lateinit var textoIniciarSesion : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_registro, container, false)

        val spinner: Spinner = view.findViewById(R.id.spinnerTipoIdentificacion)
        val tiposIdentificacion = listOf("Tipo doc", "CC", "CE", "Pasaporte")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            tiposIdentificacion
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        textoIniciarSesion = view.findViewById(R.id.linkVolverALogin)
        textoIniciarSesion.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        return view
    }


}