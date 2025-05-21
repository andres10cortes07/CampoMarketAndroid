package com.example.campomarket.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campomarket.R
import com.example.campomarket.data.model.Producto
import com.example.campomarket.data.model.Usuario
import com.example.campomarket.data.storage.ProductoManager
import com.example.campomarket.util.Constantes
import com.example.campomarket.util.NavigationUtil
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.util.*

class CrearProductoFragment : Fragment() {

    private lateinit var imagenProducto: ImageView
    private lateinit var btnSeleccionarImagen: Button
    private lateinit var campoNombreProducto: EditText
    private lateinit var campoValorPorLibra: EditText
    private lateinit var campoDisponibilidad: EditText
    private lateinit var spinnerCategoria: Spinner
    private lateinit var btnCancelarProducto: Button
    private lateinit var btnGuardarProducto: Button
    private lateinit var sharedPreferences: SharedPreferences


    private var imagenBase64: String = ""

    private val REQUEST_IMAGE_PICK = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crear_producto, container, false)
        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        // Referencias
        imagenProducto = view.findViewById(R.id.imagenProducto)
        btnSeleccionarImagen = view.findViewById(R.id.btnSeleccionarImagen)
        campoNombreProducto = view.findViewById(R.id.campoNombreProducto)
        campoValorPorLibra = view.findViewById(R.id.campoValorPorLibra)
        campoDisponibilidad = view.findViewById(R.id.campoDisponibilidad)
        spinnerCategoria = view.findViewById(R.id.spinnerCategoria)
        btnCancelarProducto = view.findViewById(R.id.btnCancelarProducto)
        btnGuardarProducto = view.findViewById(R.id.btnGuardarProducto)


        // configurar spinner
        val categorias = listOf("Frutas", "Tuberculos", "Verduras", "Quesos", "Granos")
        val adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = adapter

        btnSeleccionarImagen.setOnClickListener { seleccionarImagen() }
        btnGuardarProducto.setOnClickListener { guardarProducto() }
        btnCancelarProducto.setOnClickListener { findNavController().popBackStack() }

        return view
    }

    private fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            imagenProducto.setImageURI(uri)
            imagenBase64 = convertirImagenBase64()
        }
    }

    private fun convertirImagenBase64(): String {
        val bitmap = (imagenProducto.drawable as? BitmapDrawable)?.bitmap ?: return ""
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun guardarProducto() {
        val nombre = campoNombreProducto.text.toString().trim()
        val categoria = spinnerCategoria.selectedItem.toString()
        val valorPorLibra = campoValorPorLibra.text.toString().toIntOrNull()
        val disponibilidad = campoDisponibilidad.text.toString().toIntOrNull()

        // Obtener el JSON almacenado
        val prefs =
            requireContext().getSharedPreferences(Constantes.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val usuarioJson = prefs.getString("usuarioLogueado", null)

        val emailVendedor = if (usuarioJson != null) {
            // Deserializar el JSON a objeto Usuario
            val usuario = Gson().fromJson(usuarioJson, Usuario::class.java)
            usuario.correo
        } else {
            ""
        }

        // Validaciones
        if (nombre.isBlank() || imagenBase64.isBlank() || valorPorLibra == null || disponibilidad == null || emailVendedor.isBlank()) {
            Toast.makeText(
                requireContext(),
                "Completa todos los campos correctamente.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val producto = Producto(
            id = UUID.randomUUID().toString(),
            nombre = nombre,
            categoria = categoria,
            precioLibra = valorPorLibra,
            disponibilidad = disponibilidad,
            imagenBase64 = imagenBase64,
            vendedorEmail = emailVendedor
        )

        val productos = ProductoManager.obtenerProductos(requireContext())
        productos.add(producto)
        ProductoManager.guardarProductos(requireContext(), productos)

        Toast.makeText(requireContext(), "Producto guardado correctamente", Toast.LENGTH_SHORT)
            .show()
        findNavController().popBackStack()

    }
}
