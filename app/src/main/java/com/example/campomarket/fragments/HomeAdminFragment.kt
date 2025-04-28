package com.example.campomarket.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.util.NavigationUtil

class HomeAdminFragment : Fragment() {

    private lateinit var opcionProductos : LinearLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var ttlHome: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_admin, container, false)
        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val nombresRegistrado = sharedPreferences.getString("nombres", "") ?: ""
        ttlHome = view.findViewById(R.id.ttl_home_admin)
        ttlHome.text = "${ttlHome.text} ${nombresRegistrado}!"

        // Accede al ImageView del header directamente
        opcionProductos = view.findViewById(R.id.cardOpcionProductos)

        opcionProductos.setOnClickListener {
            findNavController().navigate(R.id.categoriasProductosFragment)
        }

        return view
    }
}