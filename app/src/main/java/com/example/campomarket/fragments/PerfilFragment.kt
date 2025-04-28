package com.example.campomarket.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.util.NavigationUtil

class PerfilFragment : Fragment() {

    private lateinit var campoNombres : EditText
    private lateinit var campoApellidos : EditText
    private lateinit var campoTipoDoc : Spinner
    private lateinit var campoIdentificacion : EditText
    private lateinit var campoCelular : EditText
    private lateinit var campoCorreo : EditText
    private lateinit var campoDepartamento : EditText
    private lateinit var campoCiudad : EditText
    private lateinit var campoDireccion : EditText
    private lateinit var btnGuardar : Button
    private lateinit var btnCancelar : Button


    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_editar_perfil, container, false)

        // Configuracion de opciones header y footer
        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        // CONFIGURACION SPINNER
        campoTipoDoc = view.findViewById(R.id.spinnerTipoDoc)
        val tiposIdentificacion = listOf("Tipo doc", "CC", "CE", "Pasaporte")

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            tiposIdentificacion
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        campoTipoDoc.adapter = adapter

        // inicializacion de variables de campos y botones
        campoNombres = view.findViewById(R.id.campoNombre)
        campoApellidos = view.findViewById(R.id.campoApellido)
        campoTipoDoc = view.findViewById(R.id.spinnerTipoDoc)
        campoIdentificacion = view.findViewById(R.id.campoIdentificacion)
        campoCelular = view.findViewById(R.id.campoCelular)
        campoCorreo = view.findViewById(R.id.campoCorreo)
        campoDepartamento = view.findViewById(R.id.campoDepartamento)
        campoCiudad = view.findViewById(R.id.campoCiudad)
        campoDireccion = view.findViewById(R.id.campoDireccion)
        btnGuardar = view.findViewById(R.id.btnGuardarEditCuenta)
        btnCancelar = view.findViewById(R.id.btnCancelarEditCuenta)

        // obtencion de datos del sharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        val nombreRegistrado = sharedPreferences.getString("nombres", "") ?: ""
        val apellidoRegistrado = sharedPreferences.getString("apellidos", "") ?: ""
        val tipoDocRegistrado = sharedPreferences.getString("tipoDocumento", "") ?: ""
        val identificacionRegistrada = sharedPreferences.getString("numeroIdentificacion", "") ?: ""
        val correoRegistrado = sharedPreferences.getString("correo", "") ?: ""
        val celularRegistrado = sharedPreferences.getString("celular", "") ?: ""
        val departamentoRegistrado = sharedPreferences.getString("departamento", "") ?: ""
        val ciudadRegistrado = sharedPreferences.getString("ciudad", "") ?: ""
        val direccionRegistrado = sharedPreferences.getString("direccion", "") ?: ""

        // carga de datos a edits texts
        campoNombres.setText(nombreRegistrado)
        campoApellidos.setText(apellidoRegistrado)

        // dejar por defecto la opcion registrada en el shared
        val indexTipoDoc = tiposIdentificacion.indexOf(tipoDocRegistrado)
        if (indexTipoDoc != -1) {
            campoTipoDoc.setSelection(indexTipoDoc)
        }

        campoIdentificacion.setText(identificacionRegistrada)
        campoCelular.setText(celularRegistrado)
        campoCorreo.setText(correoRegistrado)
        campoDepartamento.setText(departamentoRegistrado)
        campoCiudad.setText(ciudadRegistrado)
        campoDireccion.setText(direccionRegistrado)


        // actualizacion de datos
        btnGuardar.setOnClickListener {
            val nuevoNombre = campoNombres.text.toString()
            val nuevoApellido = campoApellidos.text.toString()
            val nuevoTipoDoc = campoTipoDoc.selectedItem.toString()
            val nuevaIdentificacion = campoIdentificacion.text.toString()
            val nuevoCelular = campoCelular.text.toString()
            val nuevoCorreo = campoCorreo.text.toString()
            val nuevoDepartamento = campoDepartamento.text.toString()
            val nuevaCiudad = campoCiudad.text.toString()
            val nuevaDireccion = campoDireccion.text.toString()

            // Sí hay cambios -> Actualizar SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("nombres", nuevoNombre)
            editor.putString("apellidos", nuevoApellido)
            editor.putString("tipoDocumento", nuevoTipoDoc)
            editor.putString("numeroIdentificacion", nuevaIdentificacion)
            editor.putString("celular", nuevoCelular)
            editor.putString("correo", nuevoCorreo)
            editor.putString("departamento", nuevoDepartamento)
            editor.putString("ciudad", nuevaCiudad)
            editor.putString("direccion", nuevaDireccion)
            editor.apply()

            Toast.makeText(requireContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.masOpcionesFragment)
        }

        btnCancelar.setOnClickListener {
            findNavController().navigate(R.id.masOpcionesFragment)
        }

        return view
    }
}