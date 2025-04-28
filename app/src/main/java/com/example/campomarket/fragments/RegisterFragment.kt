package com.example.campomarket.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.google.android.material.button.MaterialButton

class RegisterFragment : Fragment() {

    private lateinit var textoIniciarSesion : TextView
    private lateinit var campoNombres : EditText
    private lateinit var campoApellidos : EditText
    private lateinit var campoTipoDoc : Spinner
    private lateinit var campoNumIdentificacion : EditText
    private lateinit var campoCorreo : EditText
    private lateinit var campoCelular : EditText
    private lateinit var campoDepartamento : EditText
    private lateinit var campoCiudad : EditText
    private lateinit var campoDireccion : EditText
    private lateinit var campoRol : EditText
    private lateinit var campoClave : EditText
    private lateinit var campoValidarClave : EditText
    private lateinit var btnRegistro: MaterialButton
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_registro, container, false)


        // CONFIGURACION SPINNER
        campoTipoDoc = view.findViewById(R.id.spinnerTipoIdentificacion)
        val tiposIdentificacion = listOf("Tipo doc", "CC", "CE", "Pasaporte")

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            tiposIdentificacion
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        campoTipoDoc.adapter = adapter

        // INICIALIZACION DE VARIABLES PARA CAMPOS DE FORMULARIO
        campoNombres = view.findViewById(R.id.nombreFieldFragmentRegistro)
        campoApellidos = view.findViewById(R.id.apellidosFieldFragmentRegistro)
        campoNumIdentificacion = view.findViewById(R.id.idFieldFragmentRegistro)
        campoCorreo = view.findViewById(R.id.emailFieldFragmentRegistro)
        campoCelular = view.findViewById(R.id.celularFieldFragmentRegistro)
        campoDepartamento = view.findViewById(R.id.departamentoFieldFragmentRegistro)
        campoCiudad = view.findViewById(R.id.ciudadFieldFragmentRegistro)
        campoDireccion = view.findViewById(R.id.direccionFieldFragmentRegistro)
        campoRol = view.findViewById(R.id.rolFieldFragmentRegistro)
        campoClave = view.findViewById(R.id.passFieldFragmentRegistro)
        campoValidarClave = view.findViewById(R.id.validatePassFieldFragmentRegistro)
        btnRegistro = view.findViewById(R.id.btnRegistroFragmentoRegistro)

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        btnRegistro.setOnClickListener{
            if(validateFields()) {
                //Guardar data
                saveData()
                findNavController().navigate(R.id.loginFragment)
            }
        }

        textoIniciarSesion = view.findViewById(R.id.linkVolverALogin)
        textoIniciarSesion.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }



        return view
    }

    private fun validateFields(): Boolean {
        val nombres = campoNombres.text.toString().trim()
        val apellidos = campoApellidos.text.toString().trim()
        val correo = campoCorreo.text.toString().trim()
        val telefono = campoCelular.text.toString().trim()
        val contraseña = campoClave.text.toString().trim()
        val valContraseña = campoValidarClave.text.toString().trim()
        val rol = campoRol.text.toString().trim().lowercase()

        if (!validateCharacters(nombres, 1, 100, "El nombre debe tener entre 1 y 100 caracteres")) return false
        if (!validateCharacters(apellidos, 1, 100, "El apellido debe tener entre 1 y 100 caracteres")) return false
        if (!validateCharacters(correo, 1, 100, "El campo correo no cumple con el formato requerido")) return false

        if (!correo.contains("@") || !correo.contains(".")) {
            showToast("El correo ingresado es inválido")
            return false
        }

        if (telefono.length != 10) {
            showToast("El teléfono debe tener 10 caracteres")
            return false
        }

        if (!validateCharacters(contraseña, 1, 100, "La contraseña debe tener entre 1 y 100 caracteres")) return false
        if (!validateCharacters(valContraseña, 1, 100, "La validación de contraseña debe tener entre 1 y 100 caracteres")) return false

        if (contraseña != valContraseña) {
            showToast("Las contraseñas ingresadas son diferentes")
            return false
        }

        // Validación del rol
        val rolesValidos = listOf("vendedor", "comprador", "administrador")
        if (rol !in rolesValidos) {
            showToast("El rol debe ser vendedor, comprador o administrador")
            return false
        }

        // Comentario temporal: el rol 'administrador' solo estará permitido hasta que se cree el registro en base de datos
        return true
    }

    private fun validateCharacters(campo: String, min: Int, max: Int, msgError: String): Boolean {
        if (campo.length < min || campo.length > max) {
            showToast(msgError)
            return false
        }
        return true
    }

    private fun saveData() {
        val editor = sharedPreferences.edit()

        editor.putString("nombres", campoNombres.text.toString().trim())
        editor.putString("apellidos", campoApellidos.text.toString().trim())
        editor.putString("tipoDocumento", campoTipoDoc.selectedItem.toString())
        editor.putString("numeroIdentificacion", campoNumIdentificacion.text.toString().trim())
        editor.putString("correo", campoCorreo.text.toString().trim())
        editor.putString("celular", campoCelular.text.toString().trim())
        editor.putString("departamento", campoDepartamento.text.toString().trim())
        editor.putString("ciudad", campoCiudad.text.toString().trim())
        editor.putString("direccion", campoDireccion.text.toString().trim())
        editor.putString("rol", campoRol.text.toString().trim())
        editor.putString("clave", campoClave.text.toString().trim())

        editor.apply()

        showToast("Registro exitoso")
    }

    private fun showToast(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }

}