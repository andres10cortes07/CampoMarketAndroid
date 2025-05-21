package com.example.campomarket.util

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.campomarket.R
import com.example.campomarket.activities.AuthActivity
import com.example.campomarket.data.model.Usuario
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.Gson

object NavigationUtil {

    private fun obtenerUsuario(activity: FragmentActivity): Usuario? {
        val sharedPreferences = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val usuarioJson = sharedPreferences.getString("usuarioLogueado", null)
        return if (usuarioJson != null) {
            Gson().fromJson(usuarioJson, Usuario::class.java)
        } else {
            null
        }
    }

    private fun validarRol(activity: FragmentActivity): String {
        val usuario = obtenerUsuario(activity)
        return usuario?.rol ?: ""
    }

    fun setupHeaderAndFooter(view: View, navController: NavController, activity: FragmentActivity) {
        val rol = validarRol(activity).lowercase()
        when (rol) {
            "administrador" -> setupComponentsAdmin(view, navController, activity)
            "comprador" -> setupComponentsComprador(view, navController, activity)
            "vendedor" -> setupComponentsVendedor(view, navController, activity)

            else -> Toast.makeText(activity, "Error al validar el rol", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupComponentsAdmin(view: View, navController: NavController, activity: FragmentActivity) {
        view.findViewById<View>(R.id.includeHeaderAdmin).visibility = View.VISIBLE
        view.findViewById<View>(R.id.includeFooterAdmin).visibility = View.VISIBLE

        val includeHeaderAdmin = view.findViewById<View>(R.id.includeHeaderAdmin)
        val iconoSalir = includeHeaderAdmin?.findViewById<View>(R.id.iconoSalir)
        val iconoApp = includeHeaderAdmin?.findViewById<View>(R.id.icon)

        val includeFooterAdmin = view.findViewById<View>(R.id.includeFooterAdmin)
        val opcionProductos = includeFooterAdmin?.findViewById<View>(R.id.cardOpcionProductos)
        val opcionClientes = includeFooterAdmin?.findViewById<View>(R.id.cardOpcionClientes)
        val opcionVendedores = includeFooterAdmin?.findViewById<View>(R.id.cardOpcionVendedores)
        val opcionMas = includeFooterAdmin?.findViewById<View>(R.id.cardOpcionMas)

        iconoApp?.setOnClickListener {
            navController.navigate(R.id.homeAdminFragment)
        }

        iconoSalir?.setOnClickListener {
            // 1. Limpiar información de usuario logueado en SharedPreferences
            val prefs = activity.getSharedPreferences(Constantes.SHARED_PREF_NAME, Context.MODE_PRIVATE)
            prefs.edit().remove("usuarioLogueado").apply() // Elimina el usuario logueado de SharedPreferences

            // Se cierra sesión de Google si aplica
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val googleSignInClient = GoogleSignIn.getClient(activity, gso)

            googleSignInClient.signOut().addOnCompleteListener(activity) {
                val intent = Intent(activity, AuthActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            }
        }

        opcionProductos?.setOnClickListener {
            navController.navigate(R.id.categoriasProductosFragment)
        }

        opcionClientes?.setOnClickListener {
            navController.navigate(R.id.clientesFragment)
        }

        opcionVendedores?.setOnClickListener {
            navController.navigate(R.id.vendedoresFragment)
        }

        opcionMas?.setOnClickListener {
            navController.navigate(R.id.masOpcionesFragmentAdmin)
        }
    }

    private fun setupComponentsComprador(view: View, navController: NavController, activity: FragmentActivity) {
        view.findViewById<View>(R.id.includeHeaderComprador).visibility = View.VISIBLE
        view.findViewById<View>(R.id.includeFooterComprador).visibility = View.VISIBLE

        val includeHeaderComprador = view.findViewById<View>(R.id.includeHeaderComprador)
        val iconoApp = includeHeaderComprador?.findViewById<View>(R.id.icon)
        val iconoSalir = includeHeaderComprador?.findViewById<View>(R.id.iconoSalir)
        val iconoCarrito = includeHeaderComprador?.findViewById<View>(R.id.iconoCarrito)

        val includeFooterComprador = view.findViewById<View>(R.id.includeFooterComprador)
        val opcionProductos = includeFooterComprador?.findViewById<View>(R.id.cardOpcionProductos)
        val opcionCuenta = includeFooterComprador?.findViewById<View>(R.id.cardOpcionMiCuenta)
        val opcionTiendas = includeFooterComprador?.findViewById<View>(R.id.cardOpcionTiendas)
        val opcionMas = includeFooterComprador?.findViewById<View>(R.id.cardOpcionMas)

        iconoApp?.setOnClickListener {
            navController.navigate(R.id.homeCompradorFragment)
        }

        iconoSalir?.setOnClickListener {
            // 1. Limpiar información de usuario logueado en SharedPreferences
            val prefs = activity.getSharedPreferences(Constantes.SHARED_PREF_NAME, Context.MODE_PRIVATE)
            prefs.edit().remove("usuarioLogueado").apply() // Elimina el usuario logueado de SharedPreferences

            // Se cierra sesión de Google si aplica
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val googleSignInClient = GoogleSignIn.getClient(activity, gso)

            googleSignInClient.signOut().addOnCompleteListener(activity) {
                val intent = Intent(activity, AuthActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            }
        }

        iconoCarrito?.setOnClickListener {
            navController.navigate(R.id.carritoFragment)
        }

        opcionProductos?.setOnClickListener {
            navController.navigate(R.id.categoriasProductosFragment)
        }

        opcionCuenta?.setOnClickListener {
            navController.navigate(R.id.editarCuentaFragment)
        }

        opcionTiendas?.setOnClickListener {
            navController.navigate(R.id.tiendasFragment)
        }

        opcionMas?.setOnClickListener {
            navController.navigate(R.id.masOpcionesFragmentComprador)
        }
    }

    private fun setupComponentsVendedor(view: View, navController: NavController, activity: FragmentActivity) {
        view.findViewById<View>(R.id.includeHeaderVendedor).visibility = View.VISIBLE
        view.findViewById<View>(R.id.includeFooterVendedor).visibility = View.VISIBLE

        val includeHeaderVendedor = view.findViewById<View>(R.id.includeHeaderVendedor)
        val iconoApp = includeHeaderVendedor?.findViewById<View>(R.id.icon)
        val iconoSalir = includeHeaderVendedor?.findViewById<View>(R.id.iconoSalir)
        val iconoCarrito = includeHeaderVendedor?.findViewById<View>(R.id.iconoCarrito)

        val includeFooterVendedor = view.findViewById<View>(R.id.includeFooterVendedor)
        val opcionProductos = includeFooterVendedor?.findViewById<View>(R.id.cardOpcionProductos)
        val opcionCuenta = includeFooterVendedor?.findViewById<View>(R.id.cardOpcionMiCuenta)
        val opcionMisProductos = includeFooterVendedor?.findViewById<View>(R.id.cardOpcionMisProductos)
        val opcionMas = includeFooterVendedor?.findViewById<View>(R.id.cardOpcionMas)

        iconoApp?.setOnClickListener {
            navController.navigate(R.id.homeCompradorFragment)
        }

        iconoSalir?.setOnClickListener {
            // 1. Limpiar información de usuario logueado en SharedPreferences
            val prefs = activity.getSharedPreferences(Constantes.SHARED_PREF_NAME, Context.MODE_PRIVATE)
            prefs.edit().remove("usuarioLogueado").apply() // Elimina el usuario logueado de SharedPreferences

            // Se cierra sesión de Google si aplica
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val googleSignInClient = GoogleSignIn.getClient(activity, gso)

            googleSignInClient.signOut().addOnCompleteListener(activity) {
                val intent = Intent(activity, AuthActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            }
        }

        iconoCarrito?.setOnClickListener {
            navController.navigate(R.id.carritoFragment)
        }

        opcionProductos?.setOnClickListener {
            navController.navigate(R.id.categoriasProductosFragment)
        }

        opcionCuenta?.setOnClickListener {
            navController.navigate(R.id.editarCuentaFragment)
        }

         opcionMisProductos?.setOnClickListener {
             navController.navigate(R.id.misProductosFragment)
         }

        opcionMas?.setOnClickListener {
            navController.navigate(R.id.masOpcionesFragmentVendedor)
        }
    }
}
