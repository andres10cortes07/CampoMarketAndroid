package com.example.campomarket.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.util.NavigationUtil
import com.google.android.material.button.MaterialButton

class CrearUsuarioFragment : Fragment () {

    private lateinit var ttlCrearUsuario : TextView
    private lateinit var campoNombre : EditText
    private lateinit var campoApellidos : EditText
    private lateinit var campoTipoDoc : Spinner
    private lateinit var campoId : EditText
    private lateinit var campoCelular : EditText
    private lateinit var campoCorreo : EditText
    private lateinit var campoDepartamento : EditText
    private lateinit var campoCiudad : EditText
    private lateinit var campoDireccion : EditText
    private lateinit var btnCancelar : MaterialButton
    private lateinit var btnGuardar : MaterialButton
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crear_usuarios, container, false)
        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        ttlCrearUsuario = view.findViewById(R.id.ttl_crear_usuario)
        val fragmentOrigen = arguments?.getString("fragment_origen")

        if (fragmentOrigen == "ClientesFragment") ttlCrearUsuario.text = "Crear nuevo comprador"
        else if (fragmentOrigen == "VendedoresFragment") ttlCrearUsuario.text = "Crear nuevo vendedor"

        campoNombre = view.findViewById(R.id.campoNombre)
        campoApellidos = view.findViewById(R.id.campoApellido)
        campoTipoDoc = view.findViewById(R.id.spinnerTipoDoc)
        campoId = view.findViewById(R.id.campoIdentificacion)
        campoCelular = view.findViewById(R.id.campoCelular)
        campoCorreo = view.findViewById(R.id.campoCorreo)
        campoDepartamento = view.findViewById(R.id.campoDepartamento)
        campoCiudad = view.findViewById(R.id.campoCiudad)
        campoDireccion = view.findViewById(R.id.campoDireccion)
        btnCancelar = view.findViewById(R.id.btnCancelarCrearUsuario)
        btnGuardar = view.findViewById(R.id.btnGuardarUsuario)

        // CONFIGURACION SPINNERS
        campoTipoDoc = view.findViewById(R.id.spinnerTipoDoc)
        val tiposIdentificacion = listOf("Tipo doc", "CC", "CE", "Pasaporte")

        val adapterTipoDoc = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            tiposIdentificacion
        )
        adapterTipoDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        campoTipoDoc.adapter = adapterTipoDoc

        // VALIDACION PARA AGREGAR CUANDO SE CREE EL USUARIO
        val tiposDocValidos = listOf("CC", "CE", "Pasaporte")
        val tipoDoc = campoTipoDoc.selectedItem.toString().trim()
        if (tipoDoc !in tiposDocValidos) {
            Toast.makeText(requireContext(), "Selecciona un tipo de documento valido", Toast.LENGTH_SHORT).show()
        }

        return view
    }

}