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
import com.example.campomarket.data.model.Usuario
import com.example.campomarket.data.storage.UsuarioManager
import com.example.campomarket.util.Constantes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LoginFragment : Fragment() {

    private lateinit var campoCorreo: EditText
    private lateinit var campoClave: EditText
    private lateinit var btnLogin: Button
    private lateinit var textoRecuperarClave: TextView
    private lateinit var btnRegistro: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnGoogle: MaterialButton
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 123

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Configuración de Google Sign-In
        val googleSign = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), googleSign)

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        campoCorreo = view.findViewById(R.id.emailFieldFragmentLogin)
        campoClave = view.findViewById(R.id.passFieldFragmentLogin)
        btnGoogle = view.findViewById(R.id.btnGoogle)
        btnLogin = view.findViewById(R.id.btnLoginFragmentLogin)
        textoRecuperarClave = view.findViewById(R.id.linkRecovPassFragmentLogin)
        btnRegistro = view.findViewById(R.id.btnRegisterFragmentLogin)

        // Boton login con correo y clave
        btnLogin.setOnClickListener {
            if (validateCredentials()) {
                val correo = campoCorreo.text.toString().trim()
                procesarUsuarioSegunDatos(correo, null, null)
            } else {
                Toast.makeText(requireContext(), "Los datos ingresados son erróneos", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón login con Google
        btnGoogle.setOnClickListener {
            signIn()
        }

        textoRecuperarClave.setOnClickListener {
            findNavController().navigate(R.id.recoverPasswordFragment)
        }

        btnRegistro.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        return view
    }

    private fun validateCredentials(): Boolean {
        val correoIngresado = campoCorreo.text.toString().trim()
        val claveIngresada = campoClave.text.toString().trim()

        val listaUsuarios = UsuarioManager.obtenerUsuarios(requireContext())
        val usuarioEncontrado = listaUsuarios.find { it.correo == correoIngresado && it.clave == claveIngresada }

        return usuarioEncontrado != null
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val correo = account?.email ?: ""
            val nombreCompleto = account?.displayName ?: ""
            val nombres = nombreCompleto.substringBefore(" ")
            val apellidos = nombreCompleto.substringAfter(" ", "")

            procesarUsuarioSegunDatos(correo, nombres, apellidos)

        } catch (e: ApiException) {
            Toast.makeText(requireContext(), "Inicio de sesión fallido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun procesarUsuarioSegunDatos(correo: String, nombres: String?, apellidos: String?) {
        val listaUsuarios = UsuarioManager.obtenerUsuarios(requireContext())
        val usuario = listaUsuarios.find { it.correo == correo }

        if (usuario == null || !usuarioTieneDatosCompletos(usuario)) {
            val bundle = Bundle().apply {
                putString("nombres", nombres ?: "")
                putString("apellidos", apellidos ?: "")
                putString("correo", correo)
            }
            Toast.makeText(requireContext(), "Por último completa estos datos", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.completarPerfilFragment, bundle)
        } else {
            // Guardar el usuario logueado
            val prefs = requireContext().getSharedPreferences(Constantes.SHARED_PREF_NAME, Context.MODE_PRIVATE)
            prefs.edit().putString("usuarioLogueado", Gson().toJson(usuario)).apply()

            when (usuario.rol) {
                "administrador" -> redirectionForRol("Admin", MainActivityAdmin::class.java)
                "comprador" -> redirectionForRol("Comprador", MainActivityComprador::class.java)
                "vendedor" -> redirectionForRol("Vendedor", MainActivityVendedor::class.java)
                else -> {
                    Toast.makeText(requireContext(), "Rol inválido, no tienes permisos para navegar acá", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun usuarioTieneDatosCompletos(usuario: Usuario): Boolean {
        return usuario.tipoDocumento.isNotEmpty() &&
                usuario.numeroIdentificacion.isNotEmpty() &&
                usuario.celular.isNotEmpty() &&
                usuario.departamento.isNotEmpty() &&
                usuario.ciudad.isNotEmpty() &&
                usuario.direccion.isNotEmpty() &&
                usuario.rol.isNotEmpty()
    }

    private fun redirectionForRol(rol: String, activity: Class<*>) {
        Toast.makeText(requireContext(), "Bienvenido", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), activity)
        intent.putExtra("startAtHome$rol", true)
        startActivity(intent)
        requireActivity().finish()
    }
}