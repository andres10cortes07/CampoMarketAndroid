package com.example.campomarket.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.activities.AuthActivity
import com.example.campomarket.util.Constantes
import com.example.campomarket.util.NavigationUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MasOpcionesFragmentAdmin : Fragment() {

    private lateinit var opcionSalir : LinearLayout
    private lateinit var opcionTiendas : LinearLayout
    private lateinit var opcionCambiarClave : LinearLayout
    private lateinit var opcionCuenta : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mas_opciones_admin, container, false)

        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        opcionSalir = view.findViewById(R.id.opcionCerrarSesion)
        opcionSalir?.setOnClickListener {
            // 1. Limpiar información de usuario logueado en SharedPreferences
            val prefs = requireContext().getSharedPreferences(Constantes.SHARED_PREF_NAME, Context.MODE_PRIVATE)
            prefs.edit().remove("usuarioLogueado").apply() // Elimina el usuario logueado de SharedPreferences

            // Se cierra sesión de Google si aplica
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

            googleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()
            }
        }

        opcionTiendas = view.findViewById(R.id.opcionTiendas)
        opcionTiendas.setOnClickListener{
            findNavController().navigate(R.id.tiendasFragment)
        }

        opcionCuenta = view.findViewById(R.id.opcionEditarCuenta)
        opcionCuenta.setOnClickListener{
            findNavController().navigate(R.id.editarCuentaFragment)
        }

        opcionCambiarClave = view.findViewById(R.id.opcionCambiarClave)
        opcionCambiarClave.setOnClickListener{
            findNavController().navigate(R.id.cambiarClaveFragment)
        }

        return view
    }
}