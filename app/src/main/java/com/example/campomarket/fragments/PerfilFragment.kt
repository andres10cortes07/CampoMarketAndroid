package com.example.campomarket.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.data.model.Usuario
import com.example.campomarket.data.storage.UsuarioManager
import com.example.campomarket.util.NavigationUtil

class PerfilFragment : Fragment() {

    private lateinit var campoNombres: EditText
    private lateinit var campoApellidos: EditText
    private lateinit var campoTipoDoc: Spinner
    private lateinit var campoIdentificacion: EditText
    private lateinit var campoCelular: EditText
    private lateinit var campoCorreo: EditText
    private lateinit var campoDepartamento: EditText
    private lateinit var campoCiudad: EditText
    private lateinit var campoDireccion: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button

    private lateinit var usuario: Usuario

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_editar_perfil, container, false)

        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        campoTipoDoc = view.findViewById(R.id.spinnerTipoDoc)
        val tiposIdentificacion = listOf("Tipo doc", "CC", "CE", "Pasaporte")

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            tiposIdentificacion
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        campoTipoDoc.adapter = adapter

        campoNombres = view.findViewById(R.id.campoNombre)
        campoApellidos = view.findViewById(R.id.campoApellido)
        campoIdentificacion = view.findViewById(R.id.campoIdentificacion)
        campoCelular = view.findViewById(R.id.campoCelular)
        campoCorreo = view.findViewById(R.id.campoCorreo)
        campoDepartamento = view.findViewById(R.id.campoDepartamento)
        campoCiudad = view.findViewById(R.id.campoCiudad)
        campoDireccion = view.findViewById(R.id.campoDireccion)
        btnGuardar = view.findViewById(R.id.btnGuardarEditCuenta)
        btnCancelar = view.findViewById(R.id.btnCancelarEditCuenta)

        // Obtener usuario logueado desde UsuarioManager
        val usuarioLogueado = UsuarioManager.obtenerUsuarioLogueado(requireContext())
        if (usuarioLogueado == null) {
            Toast.makeText(requireContext(), "No se encontró usuario logueado", Toast.LENGTH_SHORT).show()
            return view
        }

        usuario = usuarioLogueado

        // Rellenar campos con datos del usuario
        campoNombres.setText(usuario.nombres)
        campoApellidos.setText(usuario.apellidos)
        campoIdentificacion.setText(usuario.numeroIdentificacion)
        campoCelular.setText(usuario.celular)
        campoCorreo.setText(usuario.correo)
        campoDepartamento.setText(usuario.departamento)
        campoCiudad.setText(usuario.ciudad)
        campoDireccion.setText(usuario.direccion)

        val indexTipoDoc = tiposIdentificacion.indexOf(usuario.tipoDocumento)
        if (indexTipoDoc != -1) {
            campoTipoDoc.setSelection(indexTipoDoc)
        }

        if (usuario.rol != "administrador") {
            campoIdentificacion.isEnabled = false
            campoIdentificacion.isFocusable = false
            campoIdentificacion.isClickable = false
        }

        btnGuardar.setOnClickListener {
            val nuevoUsuario = usuario.copy(
                nombres = campoNombres.text.toString(),
                apellidos = campoApellidos.text.toString(),
                tipoDocumento = campoTipoDoc.selectedItem.toString(),
                numeroIdentificacion = campoIdentificacion.text.toString(),
                celular = campoCelular.text.toString(),
                correo = campoCorreo.text.toString(),
                departamento = campoDepartamento.text.toString(),
                ciudad = campoCiudad.text.toString(),
                direccion = campoDireccion.text.toString()
            )

            val usuarios = UsuarioManager.obtenerUsuarios(requireContext())

            // Validar que no exista otro usuario con el mismo correo
            if (usuarios.any { it.correo == nuevoUsuario.correo && it.correo != usuario.correo }) {
                Toast.makeText(requireContext(), "Ya existe un usuario con este correo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Actualizar lista: reemplazar usuario editado
            val indice = usuarios.indexOfFirst { it.correo == usuario.correo }
            if (indice != -1) {
                usuarios[indice] = nuevoUsuario
                UsuarioManager.guardarUsuarios(requireContext(), usuarios)

                // Actualizar usuario logueado también
                val prefsEditor = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE).edit()
                prefsEditor.putString("usuarioLogueado", com.google.gson.Gson().toJson(nuevoUsuario))
                prefsEditor.apply()

                Toast.makeText(requireContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()

                when (usuario.rol) {
                    "administrador" -> findNavController().navigate(R.id.masOpcionesFragmentAdmin)
                    "comprador" -> findNavController().navigate(R.id.masOpcionesFragmentComprador)
                    "vendedor" -> findNavController().navigate(R.id.masOpcionesFragmentVendedor)
                }
            } else {
                Toast.makeText(requireContext(), "Error al actualizar usuario", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelar.setOnClickListener {
            when (usuario.rol) {
                "administrador" -> findNavController().navigate(R.id.masOpcionesFragmentAdmin)
                "comprador" -> findNavController().navigate(R.id.masOpcionesFragmentComprador)
                "vendedor" -> findNavController().navigate(R.id.masOpcionesFragmentVendedor)
            }
        }

        return view
    }
}
