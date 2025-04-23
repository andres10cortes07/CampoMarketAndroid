package com.example.campomarket.util

import android.content.Intent
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.campomarket.R
import com.example.campomarket.activities.AuthActivity

object AdminNavigationUtil {

    fun setupHeaderAndFooter(view: View, navController: NavController, activity: FragmentActivity) {
        // HEADER
        val iconoSalir = view.findViewById<View>(R.id.iconoSalir)

        // FOOTER
        val opcionProductos = view.findViewById<View>(R.id.cardOpcionProductos)
        val opcionClientes = view.findViewById<View>(R.id.cardOpcionClientes)
        val opcionVendedores = view.findViewById<View>(R.id.cardOpcionVendedores)
        val opcionMas = view.findViewById<View>(R.id.cardOpcionMas)

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
}