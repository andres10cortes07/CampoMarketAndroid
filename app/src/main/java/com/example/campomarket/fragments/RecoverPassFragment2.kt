package com.example.campomarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R

class RecoverPassFragment2 : Fragment() {

    private lateinit var textoVolverALogin : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recover_pass2, container, false)

        textoVolverALogin = view.findViewById(R.id.linkVolverALogin)
        textoVolverALogin.setOnClickListener{
            findNavController().navigate(R.id.loginFragment)
        }

        return view
    }

}