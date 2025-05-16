package com.example.campomarket.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.activities.AuthActivity
import com.example.campomarket.data.model.Usuario
import com.example.campomarket.data.storage.UsuarioManager
import com.example.campomarket.util.NavigationUtil

class CambiarClaveFragment : Fragment() {

    private lateinit var campoClaveActual: EditText
    private lateinit var campoNuevaClave: EditText
    private lateinit var campoValNuevaClave: EditText
    private lateinit var btnCambiar: Button
    private lateinit var btnCancelar: Button
    private lateinit var usuarioLogueado: Usuario

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cambiar_clave, container, false)

        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        // Obtener el usuario logueado usando UsuarioManager
        val usuario = UsuarioManager.obtenerUsuarioLogueado(requireContext())
        if (usuario == null) {
            Toast.makeText(requireContext(), "No se encontró usuario logueado", Toast.LENGTH_SHORT).show()
            return view
        }
        usuarioLogueado = usuario

        // Inicializar campos de vista
        campoClaveActual = view.findViewById(R.id.campoClaveActual)
        campoNuevaClave = view.findViewById(R.id.campoNuevaClave)
        campoValNuevaClave = view.findViewById(R.id.campoValNuevaClave)
        btnCambiar = view.findViewById(R.id.btnCambiarClave)
        btnCancelar = view.findViewById(R.id.btnCancelarCambiarClave)

        btnCambiar.setOnClickListener {
            val claveAntigua = campoClaveActual.text.toString().trim()
            val nuevaClave = campoNuevaClave.text.toString().trim()
            val valNuevaClave = campoValNuevaClave.text.toString().trim()

            val usuarios = UsuarioManager.obtenerUsuarios(requireContext())
            val index = usuarios.indexOfFirst { it.correo == usuarioLogueado.correo }

            if (index == -1) {
                Toast.makeText(requireContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuarioEncontrado = usuarios[index]

            if (usuarioEncontrado.clave != claveAntigua) {
                Toast.makeText(requireContext(), "La clave actual es incorrecta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nuevaClave.length !in 4..30) {
                Toast.makeText(requireContext(), "La nueva clave debe tener entre 4 y 30 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nuevaClave != valNuevaClave) {
                Toast.makeText(requireContext(), "Las nuevas claves no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Actualizar clave y guardar
            usuarios[index] = usuarioEncontrado.copy(clave = nuevaClave)
            UsuarioManager.guardarUsuarios(requireContext(), usuarios)

            Toast.makeText(requireContext(), "Contraseña actualizada. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }

        btnCancelar.setOnClickListener {
            when (usuarioLogueado.rol) {
                "administrador" -> findNavController().navigate(R.id.masOpcionesFragmentAdmin)
                "comprador" -> findNavController().navigate(R.id.masOpcionesFragmentComprador)
                "vendedor" -> findNavController().navigate(R.id.masOpcionesFragmentVendedor)
            }
        }

        return view
    }
}
