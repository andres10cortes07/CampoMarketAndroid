package com.example.campomarket.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.example.campomarket.activities.MainActivityAdmin
import com.example.campomarket.activities.MainActivityComprador
import com.example.campomarket.activities.MainActivityVendedor

class LoginFragment : Fragment() {

    private lateinit var campoCorreo : EditText
    private lateinit var campoClave : EditText
    private lateinit var btnLogin : Button
    private lateinit var textoRecuperarClave : TextView
    private lateinit var btnRegistro : Button
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        campoCorreo = view.findViewById(R.id.emailFieldFragmentLogin)
        campoClave = view.findViewById(R.id.passFieldFragmentLogin)

        btnLogin = view.findViewById(R.id.btnLoginFragmentLogin)
        btnLogin.setOnClickListener {
            if (validateCredentials()) {
                val rolRegistrado = sharedPreferences.getString("rol", "") ?: ""
                when (rolRegistrado) {
                    "administrador" -> redirectionForRol("Admin", MainActivityAdmin::class.java)
                    "comprador" -> redirectionForRol("Comprador", MainActivityComprador::class.java)
                    "vendedor" -> redirectionForRol("Vendedor", MainActivityVendedor::class.java)
                    else -> {
                        Toast.makeText(requireContext(), "PA FUERA", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else Toast.makeText(requireContext(), "Los datos ingresados son erroneos", Toast.LENGTH_SHORT).show()
        }

        textoRecuperarClave = view.findViewById(R.id.linkRecovPassFragmentLogin)
        textoRecuperarClave.setOnClickListener {
            findNavController().navigate(R.id.recoverPasswordFragment)
        }

        btnRegistro = view.findViewById(R.id.btnRegisterFragmentLogin)
        btnRegistro.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        return view
    }

    private fun validateCredentials(): Boolean {
        val campoEmail = campoCorreo.text.toString().trim()
        val campoPassword = campoClave.text.toString().trim()

        val sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val correoRegistrado = sharedPreferences.getString("correo", "") ?: ""
        val claveRegistrada = sharedPreferences.getString("clave", "") ?: ""

        if (correoRegistrado.length < 5 || correoRegistrado.length > 50) return false
        if (claveRegistrada.length < 3 || claveRegistrada.length > 50) return false

        return correoRegistrado == campoEmail && claveRegistrada == campoPassword
    }

    private fun redirectionForRol (rol : String, activity : Class<*>) {
        Toast.makeText(requireContext(), "Bienvenido", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), activity)
        intent.putExtra("startAtHome${rol}", true)
        startActivity(intent)
        requireActivity().finish()
    }

}