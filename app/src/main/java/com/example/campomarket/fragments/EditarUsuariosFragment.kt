package com.example.campomarket.fragments

import android.os.Bundle
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
import com.example.campomarket.data.model.Usuario
import com.example.campomarket.data.storage.UsuarioManager
import com.example.campomarket.util.NavigationUtil

class EditarUsuariosFragment : Fragment() {

    private lateinit var campoNombre: EditText
    private lateinit var campoApellido: EditText
    private lateinit var campoTipoDoc: Spinner
    private lateinit var spinnerTipoDoc: Spinner
    private lateinit var campoIdentificacion: EditText
    private lateinit var campoCelular: EditText
    private lateinit var campoCorreo: EditText
    private lateinit var campoDepartamento: EditText
    private lateinit var campoCiudad: EditText
    private lateinit var campoDireccion: EditText
    private lateinit var btnCancelar: Button
    private lateinit var btnGuardar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_editar_usuario, container, false)
        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        // inicializacion
        campoNombre = view.findViewById(R.id.campoNombre)
        campoApellido = view.findViewById(R.id.campoApellido)
        spinnerTipoDoc = view.findViewById(R.id.spinnerTipoDoc)
        campoIdentificacion = view.findViewById(R.id.campoIdentificacion)
        campoCelular = view.findViewById(R.id.campoCelular)
        campoCorreo = view.findViewById(R.id.campoCorreo)
        campoDepartamento = view.findViewById(R.id.campoDepartamento)
        campoCiudad = view.findViewById(R.id.campoCiudad)
        campoDireccion = view.findViewById(R.id.campoDireccion)
        btnCancelar = view.findViewById(R.id.btnCancelarEditUsuario)
        btnGuardar = view.findViewById(R.id.btnGuardarUsuario)

        // Cargar datos del Bundle
        val args = arguments
        args?.let {
            campoNombre.setText(it.getString("nombres"))
            campoApellido.setText(it.getString("apellidos"))
            campoIdentificacion.setText(it.getString("numeroIdentificacion"))
            campoCelular.setText(it.getString("celular"))
            campoCorreo.setText(it.getString("correo"))
            campoDepartamento.setText(it.getString("departamento"))
            campoCiudad.setText(it.getString("ciudad"))
            campoDireccion.setText(it.getString("direccion"))

            // Configurar Spinner con el valor recibido
            val tipoDoc = it.getString("tipoDocumento")
            campoTipoDoc = view.findViewById(R.id.spinnerTipoDoc)
            val tiposIdentificacion = listOf("Tipo doc", "CC", "CE", "Pasaporte")

            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                tiposIdentificacion
            )
            adapter.setDropDownViewResource(R.layout.spinner_item)
            campoTipoDoc.adapter = adapter

            val position = adapter.getPosition(tipoDoc)
            if (position >= 0) spinnerTipoDoc.setSelection(position)
        }

        // Botón cancelar
        btnCancelar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val usuarioExistente = UsuarioManager.buscarUsuarioPorCorreo(requireContext(), campoCorreo.text.toString().trim())

        // Botón guardar
        btnGuardar.setOnClickListener {
            val nuevoUsuario = Usuario(
                nombres = campoNombre.text.toString().trim(),
                apellidos = campoApellido.text.toString().trim(),
                tipoDocumento = spinnerTipoDoc.selectedItem.toString(),
                numeroIdentificacion = campoIdentificacion.text.toString().trim(),
                celular = campoCelular.text.toString().trim(),
                correo = campoCorreo.text.toString().trim(),
                departamento = campoDepartamento.text.toString().trim(),
                ciudad = campoCiudad.text.toString().trim(),
                direccion = campoDireccion.text.toString().trim(),
                rol = usuarioExistente?.rol ?: "",
                clave = usuarioExistente?.clave ?: ""
            )

            val actualizado = UsuarioManager.actualizarUsuario(requireContext(), nuevoUsuario)
            if (actualizado) {
                Toast.makeText(requireContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "No se pudo actualizar", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
