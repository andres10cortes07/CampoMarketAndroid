package com.example.campomarket.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.campomarket.util.NavigationUtil

class CambiarClaveFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var campoClaveActual : EditText
    private lateinit var campoNuevaClave : EditText
    private lateinit var campoValNuevaClave : EditText
    private lateinit var btnCambiar : Button
    private lateinit var btnCancelar : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cambiar_clave, container, false)

        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // inicializacion de variables de campos y botones
        campoClaveActual = view.findViewById(R.id.campoClaveActual)
        campoNuevaClave = view.findViewById(R.id.campoNuevaClave)
        campoValNuevaClave = view.findViewById(R.id.campoValNuevaClave)
        btnCambiar = view.findViewById(R.id.btnCambiarClave)
        btnCancelar = view.findViewById(R.id.btnCancelarCambiarClave)

        btnCambiar.setOnClickListener {
            val claveAntiguaIngresada = campoClaveActual.text.toString().trim()
            val claveNuevaIngresada = campoNuevaClave.text.toString().trim()
            val claveNuevaValIngresada = campoValNuevaClave.text.toString().trim()

            val claveRegistrada = sharedPreferences.getString("clave", "") ?: ""

            if (claveRegistrada != claveAntiguaIngresada) {
                Toast.makeText(requireContext(), "La clave ingresada no coincide con la actual", Toast.LENGTH_SHORT).show()
            }
            else {
                if (claveNuevaIngresada.length < 3 || claveNuevaIngresada.length > 30 ||
                    claveNuevaValIngresada.length < 3 || claveNuevaValIngresada.length > 30) {
                    Toast.makeText(requireContext(), "La nueva clave debe tener entre 4 y 30 caracteres", Toast.LENGTH_SHORT).show()
                }
                else {
                    if (claveNuevaIngresada == claveNuevaValIngresada){
                        val editor = sharedPreferences.edit()
                        editor.putString("clave", claveNuevaIngresada)
                        editor.apply()

                        // EN ESTE CASO CUANDO SE LE CIERRA SESION AL USUARIO
                        Toast.makeText(requireContext(), "Cambio de contraseña exitoso, inicia sesión de nuevo", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), AuthActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    else {
                        Toast.makeText(requireContext(), "Las claves ingresadas no coinciden entre si", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnCancelar.setOnClickListener {
            findNavController().navigate(R.id.masOpcionesFragment)
        }



        return view
    }
}