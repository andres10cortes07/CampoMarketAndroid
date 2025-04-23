package com.example.campomarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.util.AdminNavigationUtil

class CategoriasProductosFragment : Fragment() {

    private lateinit var opcionTodosLosProductos : LinearLayout
    private lateinit var opcionFrutas : LinearLayout
    private lateinit var opcionGranos : LinearLayout
    private lateinit var opcionVerduras : LinearLayout
    private lateinit var opcionTuberculos : LinearLayout
    private lateinit var opcionQuesos : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categorias_productos, container, false)
        AdminNavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        opcionTodosLosProductos = view.findViewById(R.id.opcionTodosLosProductos)
        opcionTodosLosProductos.setOnClickListener {
            findNavController().navigate(R.id.TodosLosProductosFragment)
        }

        opcionFrutas = view.findViewById(R.id.opcionFrutas)
        opcionFrutas.setOnClickListener {
            findNavController().navigate(R.id.CategoriaFrutasFragment)
        }

        opcionGranos = view.findViewById(R.id.opcionGranos)
        opcionGranos.setOnClickListener {
            findNavController().navigate(R.id.CategoriaGranosFragment)
        }

        opcionVerduras = view.findViewById(R.id.opcionVerduras)
        opcionVerduras.setOnClickListener {
            findNavController().navigate(R.id.CategoriaVerdurasFragment)
        }

        opcionTuberculos = view.findViewById(R.id.opcionTuberculos)
        opcionTuberculos.setOnClickListener {
            findNavController().navigate(R.id.CategoriaTuberculosFragment)
        }

        opcionQuesos = view.findViewById(R.id.opcionQuesos)
        opcionQuesos.setOnClickListener {
            findNavController().navigate(R.id.CategoriaQuesosFragment)
        }

        return view
    }

}