package com.example.campomarket.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.campomarket.R
import com.example.campomarket.activities.AuthActivity

class InicioFragment : Fragment() {

    private lateinit var iconoLogin : ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inicio, container, false)

        iconoLogin = view.findViewById(R.id.iconoEntrar)

        iconoLogin.setOnClickListener {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
            requireActivity()
        }

        return view
    }
}