package com.example.campomarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.util.PasswordRecoveryUtil
import com.example.campomarket.data.storage.UsuarioManager
import com.example.campomarket.util.MailSender

class RecoverPassFragment : Fragment() {

    private lateinit var btnRecuperarClave: Button
    private lateinit var campoId: EditText
    private lateinit var textoVolverALogin: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recover_pass, container, false)

        campoId = view.findViewById(R.id.idFieldFragmentRecovPass)
        btnRecuperarClave = view.findViewById(R.id.btnRecuperarClave)
        textoVolverALogin = view.findViewById(R.id.linkVolverALogin)

        textoVolverALogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        btnRecuperarClave.setOnClickListener {
            val identificacion = campoId.text.toString().trim()

            if (identificacion.length < 6 || identificacion.length > 14) {
                Toast.makeText(
                    requireActivity(),
                    "La identificación debe tener entre 6 y 14 caracteres",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val listaUsuarios = UsuarioManager.obtenerUsuarios(requireContext())
            if (listaUsuarios.isEmpty()) {
                Toast.makeText(requireActivity(), "No hay usuarios registrados", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val indexUsuario = listaUsuarios.indexOfFirst { it.numeroIdentificacion == identificacion }
            if (indexUsuario == -1) {
                Toast.makeText(requireActivity(), "La identificación ingresada no está registrada", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuario = listaUsuarios[indexUsuario]
            val nuevaClave = generarClaveAleatoria()

            val asunto = "Recuperación de contraseña - CampoMarket"
            val mensaje = """
                <!DOCTYPE html>
                <html>
                <head><meta charset="UTF-8"><title>Recuperación de Contraseña</title></head>
                <body style="margin: 0; font-family: Arial, sans-serif; background-color: #FFFFFF;">
                    <div style="background-color: #4CAF50; padding: 20px; text-align: center; color: white;">
                        <h1 style="margin: 0; font-size: 26px;">Recuperación de Contraseña</h1>
                    </div>
                    <div style="padding: 30px;">
                        <h2 style="color: #FF7E05;">Hola,</h2>
                        <p style="font-size: 16px; color: #212121;">Has solicitado recuperar tu contraseña. Aquí tienes una nueva clave temporal:</p>
                        <div style="background-color: #F5F5F5; border-left: 6px solid #5C9EAD; padding: 15px; margin: 20px 0;">
                            <p style="font-size: 18px; color: #000;"><strong>👉 $nuevaClave 👈</strong></p>
                        </div>
                        <p style="font-size: 16px; color: #212121;">Por favor, inicia sesión con esta clave y cámbiala lo antes posible desde tu perfil.</p>
                        <p style="font-size: 14px; color: #5C9EAD;">Si no solicitaste este cambio, ignora este mensaje o contáctanos.</p>
                    </div>
                    <div style="background-color: #4CAF50; text-align: center; padding: 15px; color: white; font-size: 14px;">
                        © 2025 CampoMarket – Todos los derechos reservados.
                    </div>
                </body>
                </html>
            """.trimIndent()

            try {
                val sender = MailSender(
                    "campomarketapp@gmail.com",
                    "adzs lyof pvig bcpd"
                )
                sender.sendMail(usuario.correo, asunto, mensaje)

                val usuarioActualizado = usuario.copy(clave = nuevaClave)
                listaUsuarios[indexUsuario] = usuarioActualizado

                UsuarioManager.guardarUsuarios(requireContext(), listaUsuarios)

                Toast.makeText(requireContext(), "Hemos enviado una nueva clave a tu correo", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.loginFragment)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error al enviar el correo", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun generarClaveAleatoria(longitud: Int = 10): String {
        val caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..longitud)
            .map { caracteres.random() }
            .joinToString("")
    }
}
