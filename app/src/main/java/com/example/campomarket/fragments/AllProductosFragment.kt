package com.example.campomarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.util.NavigationUtil

class AllProductosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_productos, container, false)

        NavigationUtil.setupHeaderAndFooter(view, findNavController(),requireActivity())

        return view
    }

}