package com.example.campomarket.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.campomarket.R
import com.google.android.material.button.MaterialButton
import com.example.campomarket.activities.MainActivityComprador
import com.example.campomarket.data.model.Usuario
import com.example.campomarket.data.storage.UsuarioManager
import com.google.gson.Gson

class CompletarPerfilFragment : Fragment() {

    private lateinit var campoTipoDoc : Spinner
    private lateinit var campoId : EditText
    private lateinit var campoCelular : EditText
    private lateinit var campoDepartamento : EditText
    private lateinit var campoCiudad : EditText
    private lateinit var campoDireccion : EditText
    private lateinit var campoRol : Spinner
    private lateinit var campoClave : EditText
    private lateinit var campoValClave : EditText
    private lateinit var btnCompletar : MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.completar_campos_fragment, container, false)

        // CONFIGURACION SPINNER
        campoTipoDoc = view.findViewById(R.id.spinnerTipoIdentificacion)
        val tiposIdentificacion = listOf("Tipo doc", "CC", "CE", "Pasaporte")

        val adapterTipoDoc = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            tiposIdentificacion
        )
        adapterTipoDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        campoTipoDoc.adapter = adapterTipoDoc

        campoRol = view.findViewById(R.id.spinnerRol)
        val roles = listOf("Rol", "comprador", "vendedor")

        val adapterRol = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            roles
        )
        adapterRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        campoRol.adapter = adapterRol

        campoId = view.findViewById(R.id.idFieldFragmentRegistro)
        campoCelular = view.findViewById(R.id.celularFieldFragmentRegistro)
        campoDepartamento = view.findViewById(R.id.departamentoFieldFragmentRegistro)
        campoCiudad = view.findViewById(R.id.ciudadFieldFragmentRegistro)
        campoDireccion = view.findViewById(R.id.direccionFieldFragmentRegistro)
        campoClave = view.findViewById(R.id.passFieldFragmentRegistro)
        campoValClave = view.findViewById(R.id.validatePassFieldFragmentRegistro)
        btnCompletar = view.findViewById(R.id.btnFinalizarRegistro)

        btnCompletar.setOnClickListener {
            if(validateFields()) {
                saveData()
                redirectToComprador()
            }
        }

        return view
    }

    private fun validateFields(): Boolean {
        val id = campoId.text.toString().trim()
        val celular = campoCelular.text.toString().trim()
        val departamento = campoDepartamento.text.toString().trim()
        val ciudad = campoCiudad.text.toString().trim()
        val direccion = campoDireccion.text.toString().trim()
        val clave = campoClave.text.toString().trim()
        val valClave = campoValClave.text.toString().trim()
        val tipoDoc = campoTipoDoc.selectedItem.toString()
        val rol = campoRol.selectedItem.toString()

        val tiposDocValidos = listOf("CC", "CE", "Pasaporte")
        if (tipoDoc !in tiposDocValidos) {
            showToast("Selecciona un tipo de documento válido")
            return false
        }

        if (id.isEmpty()) {
            showToast("El número de identificación es obligatorio")
            return false
        }

        if (celular.length != 10) {
            showToast("El teléfono debe tener 10 caracteres")
            return false
        }

        if (departamento.isEmpty()) {
            showToast("El departamento es obligatorio")
            return false
        }

        if (ciudad.isEmpty()) {
            showToast("La ciudad es obligatoria")
            return false
        }

        if (direccion.isEmpty()) {
            showToast("La dirección es obligatoria")
            return false
        }

        val rolesValidos = listOf("comprador", "vendedor")
        if (rol !in rolesValidos) {
            showToast("Selecciona un rol válido")
            return false
        }

        if (clave.length < 6) {
            showToast("La contraseña debe tener al menos 6 caracteres")
            return false
        }

        if (clave != valClave) {
            showToast("Las contraseñas no coinciden")
            return false
        }

        return true
    }

    private fun saveData() {
        val usuario = Usuario(
            nombres = arguments?.getString("nombres") ?: "",
            apellidos = arguments?.getString("apellidos") ?: "",
            correo = arguments?.getString("correo") ?: "",
            tipoDocumento = campoTipoDoc.selectedItem.toString(),
            numeroIdentificacion = campoId.text.toString().trim(),
            celular = campoCelular.text.toString().trim(),
            departamento = campoDepartamento.text.toString().trim(),
            ciudad = campoCiudad.text.toString().trim(),
            direccion = campoDireccion.text.toString().trim(),
            rol = campoRol.selectedItem.toString().trim(),
            clave = campoClave.text.toString().trim()
        )

        val agregado = UsuarioManager.agregarUsuario(requireContext(), usuario)

        if (agregado) {
            // Guardar también como usuario logueado
            val prefs = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("usuarioLogueado", Gson().toJson(usuario))
            editor.apply()

            showToast("Perfil completado exitosamente")
        } else {
            showToast("Ya existe un usuario con ese correo")
        }
    }


    private fun redirectToComprador() {
        Toast.makeText(requireContext(), "Bienvenido", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), MainActivityComprador::class.java)
        intent.putExtra("startAtHomeComprador", true)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showToast(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }
}
