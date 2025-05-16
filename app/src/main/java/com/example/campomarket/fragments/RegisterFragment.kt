package com.example.campomarket.fragments

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
import com.example.campomarket.data.model.Usuario
import com.example.campomarket.data.storage.UsuarioManager
import com.google.android.material.button.MaterialButton

class RegisterFragment : Fragment() {

    private lateinit var textoIniciarSesion: TextView
    private lateinit var campoNombres: EditText
    private lateinit var campoApellidos: EditText
    private lateinit var campoTipoDoc: Spinner
    private lateinit var campoNumIdentificacion: EditText
    private lateinit var campoCorreo: EditText
    private lateinit var campoCelular: EditText
    private lateinit var campoDepartamento: EditText
    private lateinit var campoCiudad: EditText
    private lateinit var campoDireccion: EditText
    private lateinit var campoRol: Spinner
    private lateinit var campoClave: EditText
    private lateinit var campoValidarClave: EditText
    private lateinit var btnRegistro: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registro, container, false)

        // Configurar spinners
        campoTipoDoc = view.findViewById(R.id.spinnerTipoIdentificacion)
        val tiposIdentificacion = listOf("Tipo doc", "CC", "CE", "Pasaporte")
        val adapterTipoDoc = ArrayAdapter(requireContext(), R.layout.spinner_item, tiposIdentificacion)
        adapterTipoDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        campoTipoDoc.adapter = adapterTipoDoc

        campoRol = view.findViewById(R.id.rolFieldFragmentRegistro)
        val tiposRol = listOf("Rol", "comprador", "vendedor")
        val adapterRol = ArrayAdapter(requireContext(), R.layout.spinner_item, tiposRol)
        adapterRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        campoRol.adapter = adapterRol

        // Inicializar campos del formulario
        campoNombres = view.findViewById(R.id.nombreFieldFragmentRegistro)
        campoApellidos = view.findViewById(R.id.apellidosFieldFragmentRegistro)
        campoNumIdentificacion = view.findViewById(R.id.idFieldFragmentRegistro)
        campoCorreo = view.findViewById(R.id.emailFieldFragmentRegistro)
        campoCelular = view.findViewById(R.id.celularFieldFragmentRegistro)
        campoDepartamento = view.findViewById(R.id.departamentoFieldFragmentRegistro)
        campoCiudad = view.findViewById(R.id.ciudadFieldFragmentRegistro)
        campoDireccion = view.findViewById(R.id.direccionFieldFragmentRegistro)
        campoClave = view.findViewById(R.id.passFieldFragmentRegistro)
        campoValidarClave = view.findViewById(R.id.validatePassFieldFragmentRegistro)
        btnRegistro = view.findViewById(R.id.btnRegistroFragmentoRegistro)

        btnRegistro.setOnClickListener {
            if (validateFields()) {
                saveData()
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
        val tipoDoc = campoTipoDoc.selectedItem.toString().trim()
        val rol = campoRol.selectedItem.toString().trim().lowercase()

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

        val rolesValidos = listOf("vendedor", "comprador")
        if (rol !in rolesValidos) {
            showToast("Selecciona un rol válido")
            return false
        }

        val tiposDocValidos = listOf("CC", "CE", "Pasaporte")
        if (tipoDoc !in tiposDocValidos) {
            showToast("Selecciona un tipo de documento válido")
            return false
        }

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
        val usuario = Usuario(
            nombres = campoNombres.text.toString().trim(),
            apellidos = campoApellidos.text.toString().trim(),
            tipoDocumento = campoTipoDoc.selectedItem.toString(),
            numeroIdentificacion = campoNumIdentificacion.text.toString().trim(),
            correo = campoCorreo.text.toString().trim(),
            celular = campoCelular.text.toString().trim(),
            departamento = campoDepartamento.text.toString().trim(),
            ciudad = campoCiudad.text.toString().trim(),
            direccion = campoDireccion.text.toString().trim(),
            rol = campoRol.selectedItem.toString().trim(),
            clave = campoClave.text.toString().trim()
        )

        // Usa UsuarioManager para agregar usuario y validar duplicados
        val agregado = UsuarioManager.agregarUsuario(requireContext(), usuario)
        if (!agregado) {
            showToast("Ya existe un usuario con este correo")
            return
        }

        showToast("Registro exitoso")
        findNavController().navigate(R.id.loginFragment)
    }

    private fun showToast(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }
}
