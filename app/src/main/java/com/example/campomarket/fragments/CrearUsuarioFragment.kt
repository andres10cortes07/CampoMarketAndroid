package com.example.campomarket.fragments

import android.content.SharedPreferences
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
import com.example.campomarket.util.PasswordRecoveryUtil
import com.google.android.material.button.MaterialButton

class CrearUsuarioFragment : Fragment() {

    private lateinit var ttlCrearUsuario: TextView
    private lateinit var campoNombre: EditText
    private lateinit var campoApellidos: EditText
    private lateinit var campoTipoDoc: Spinner
    private lateinit var campoId: EditText
    private lateinit var campoCelular: EditText
    private lateinit var campoCorreo: EditText
    private lateinit var campoDepartamento: EditText
    private lateinit var campoCiudad: EditText
    private lateinit var campoDireccion: EditText
    private lateinit var btnCancelar: MaterialButton
    private lateinit var btnGuardar: MaterialButton
    private var fragmentOrigen: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crear_usuarios, container, false)
        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        fragmentOrigen = arguments?.getString("fragment_origen")

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

        ttlCrearUsuario = view.findViewById(R.id.ttl_crear_usuario)
        val fragmentOrigen = arguments?.getString("fragment_origen")

        if (fragmentOrigen == "ClientesFragment") ttlCrearUsuario.text = "Crear nuevo comprador"
        else if (fragmentOrigen == "VendedoresFragment") ttlCrearUsuario.text = "Crear nuevo vendedor"

        // Configurar spinner de tipo de documento
        val tiposIdentificacion = listOf("CC", "CE", "Pasaporte")
        val adapterTipoDoc = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            tiposIdentificacion
        )
        adapterTipoDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        campoTipoDoc.adapter = adapterTipoDoc

        btnCancelar.setOnClickListener {
            findNavController().popBackStack()
        }

        btnGuardar.setOnClickListener {
            guardarUsuario()
        }

        return view
    }

    private fun guardarUsuario() {
        val nombre = campoNombre.text.toString().trim()
        val apellidos = campoApellidos.text.toString().trim()
        val tipoDoc = campoTipoDoc.selectedItem.toString().trim()
        val id = campoId.text.toString().trim()
        val celular = campoCelular.text.toString().trim()
        val correo = campoCorreo.text.toString().trim()
        val departamento = campoDepartamento.text.toString().trim()
        val ciudad = campoCiudad.text.toString().trim()
        val direccion = campoDireccion.text.toString().trim()

        // Validaciones
        if (!validarCampo(nombre, "Nombres") ||
            !validarCampo(apellidos, "Apellidos") ||
            !validarCampo(departamento, "Departamento") ||
            !validarCampo(ciudad, "Ciudad") ||
            !validarCampo(direccion, "Dirección") ||
            !validarLongitudExacta(celular, 10, "Celular") ||
            !validarLongitudMax(id, 12, "Identificación") ||
            correo.length !in 3..50
        ) return

        // Determinar rol
        val rol = when (fragmentOrigen) {
            "ClientesFragment" -> "comprador"
            "VendedoresFragment" -> "vendedor"
            else -> "Comprador"
        }

        val nuevaClave = PasswordRecoveryUtil.generarClaveAleatoria()

        val usuario = Usuario(
            nombres = nombre,
            apellidos = apellidos,
            tipoDocumento = tipoDoc,
            numeroIdentificacion = id,
            correo = correo,
            celular = celular,
            departamento = departamento,
            ciudad = ciudad,
            direccion = direccion,
            rol = rol,
            clave = nuevaClave
        )

        if (PasswordRecoveryUtil.enviarCorreoRecuperacion(usuario, nuevaClave, "Asignacion de contraseña")) {
            val agregado = UsuarioManager.agregarUsuario(requireContext(), usuario)

            if (agregado) {
                Toast.makeText(requireContext(), "Usuario registrado y correo enviado", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Ya existe un usuario con ese correo", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Error al enviar el correo", Toast.LENGTH_SHORT).show()
        }

    }

    private fun validarCampo(campo: String, nombreCampo: String): Boolean {
        if (campo.length !in 3..50) {
            Toast.makeText(requireContext(), "$nombreCampo debe tener entre 3 y 50 caracteres", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validarLongitudExacta(campo: String, longitud: Int, nombreCampo: String): Boolean {
        if (campo.length != longitud) {
            Toast.makeText(requireContext(), "$nombreCampo debe tener exactamente $longitud dígitos", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validarLongitudMax(campo: String, max: Int, nombreCampo: String): Boolean {
        if (campo.length >= max) {
            Toast.makeText(requireContext(), "$nombreCampo debe tener menos de $max caracteres", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
