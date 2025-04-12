package com.example.campomarket.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.activities.MainActivity

class LoginFragment : Fragment() {

    private lateinit var btnRegistro : Button
    private lateinit var textoRecuperarClave : TextView
    private lateinit var btnLogin : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        btnRegistro = view.findViewById(R.id.btnRegisterFragmentLogin)
        btnRegistro.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        textoRecuperarClave = view.findViewById(R.id.linkRecovPassFragmentLogin)
        textoRecuperarClave.setOnClickListener {
            findNavController().navigate(R.id.recoverPasswordFragment)
        }

        btnLogin = view.findViewById(R.id.btnLoginFragmentLogin)
        btnLogin.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("startAtHomeAdmin", true)
            startActivity(intent)
            requireActivity().finish()
        }


        return view
    }

}