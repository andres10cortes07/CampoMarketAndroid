package com.example.campomarket.util

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.campomarket.R
import com.example.campomarket.activities.AuthActivity

object NavigationUtil {
    
    private lateinit var sharedPreferences: SharedPreferences

    private fun validarRol(activity: FragmentActivity) : String{
        sharedPreferences = activity.getSharedPreferences("UserData", FragmentActivity.MODE_PRIVATE)
        return sharedPreferences.getString("rol", "") ?: ""
    }

    fun setupHeaderAndFooter (view: View, navController: NavController, activity: FragmentActivity) {
        val rol = validarRol(activity)
        when (rol) {
            "administrador" -> setupComponentsAdmin(view, navController, activity)
            "comprador" -> setupComponentsComprador(view, navController, activity)
//            "vendedor" -> setupComponentsVendedor()

            else -> Toast.makeText(activity, "Error al validar el rol", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupComponentsAdmin(view: View, navController: NavController, activity: FragmentActivity) {
        // HACER VISIBLES HEADER Y FOOTER DE ADMIN
        view.findViewById<View>(R.id.includeHeaderAdmin).visibility = View.VISIBLE
        view.findViewById<View>(R.id.includeFooterAdmin).visibility = View.VISIBLE

        // HEADER
        val includeHeaderAdmin = view.findViewById<View>(R.id.includeHeaderAdmin)
        val iconoSalir = includeHeaderAdmin?.findViewById<View>(R.id.iconoSalir)
        val iconoApp = includeHeaderAdmin?.findViewById<View>(R.id.icon)

        // OPCIONES DE FOOTER ADMIN
        val includeFooterAdmin = view.findViewById<View>(R.id.includeFooterAdmin)
        val opcionProductos = includeFooterAdmin?.findViewById<View>(R.id.cardOpcionProductos)
        val opcionClientes = includeFooterAdmin?.findViewById<View>(R.id.cardOpcionClientes)
        val opcionVendedores = includeFooterAdmin?.findViewById<View>(R.id.cardOpcionVendedores)
        val opcionMas = includeFooterAdmin?.findViewById<View>(R.id.cardOpcionMas)

        iconoApp?.setOnClickListener {
            navController.navigate(R.id.homeAdminFragment)
        }

        iconoSalir?.setOnClickListener {
            val intent = Intent(activity, AuthActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
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
            navController.navigate(R.id.masOpcionesFragment)
        }
    }

    private fun setupComponentsComprador(view: View, navController: NavController, activity: FragmentActivity) {
        // HACER VISIBLES HEADER Y FOOTER DE COMPRADOR
        view.findViewById<View>(R.id.includeHeaderComprador).visibility = View.VISIBLE
        view.findViewById<View>(R.id.includeFooterComprador).visibility = View.VISIBLE

        // HEADER
        val includeHeaderComprador = view.findViewById<View>(R.id.includeHeaderComprador)
        val iconoApp = includeHeaderComprador?.findViewById<View>(R.id.icon)
        val iconoSalir = includeHeaderComprador?.findViewById<View>(R.id.iconoSalir)
        val iconoCarrito = includeHeaderComprador?.findViewById<View>(R.id.iconoCarrito)

        // OPCIONES DE FOOTER COMPRADOR
        val opcionProductos = view.findViewById<View>(R.id.cardOpcionProductos)
        val opcionCuenta = view.findViewById<View>(R.id.cardOpcionMiCuenta)
        val opcionTiendas = view.findViewById<View>(R.id.cardOpcionTiendas)
        val opcionMas = view.findViewById<View>(R.id.cardOpcionMas)

//        iconoApp?.setOnClickListener {
//            navController.navigate(R.id.homeAdminFragment)
//        }

        iconoSalir?.setOnClickListener {
            val intent = Intent(activity, AuthActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
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
            navController.navigate(R.id.masOpcionesFragment)
        }
    }
}