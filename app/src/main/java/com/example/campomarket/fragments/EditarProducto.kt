package com.example.campomarket.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
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

class EditarProducto : Fragment() {

    private lateinit var imagenProducto: ImageView
    private lateinit var btnCambiarImagen: Button
    private lateinit var campoNombreProducto: EditText
    private lateinit var campoValorPorLibra: EditText
    private lateinit var campoDisponibilidad: EditText
    private lateinit var spinnerCategoria: Spinner
    private lateinit var btnCancelarProducto: Button
    private lateinit var btnGuardarCambios: Button

    private var imagenBase64: String = ""
    private var productoId: String? = null

    private val REQUEST_IMAGE_PICK = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_editar_producto, container, false)
        NavigationUtil.setupHeaderAndFooter(view, findNavController(), requireActivity())

        imagenProducto = view.findViewById(R.id.imgEditarProducto)
        btnCambiarImagen = view.findViewById(R.id.btnSeleccionarImagen)
        campoNombreProducto = view.findViewById(R.id.nombre_producto_campo)
        campoValorPorLibra = view.findViewById(R.id.valor_libra_campo)
        campoDisponibilidad = view.findViewById(R.id.disponibilidad_campo)
        spinnerCategoria = view.findViewById(R.id.spinnerCategoria)
        btnCancelarProducto = view.findViewById(R.id.btnCancelarEditProducto)
        btnGuardarCambios = view.findViewById(R.id.btnGuardarEditProducto)

        // Spinner categorías
        val categorias = listOf("Frutas", "Tuberculos", "Verduras", "Quesos", "Granos y legumbres", "Otros")
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = adapter

        // Cargar datos del bundle
        cargarDatosBundle()

        btnCambiarImagen.setOnClickListener { seleccionarImagen() }
        btnGuardarCambios.setOnClickListener { guardarCambios() }
        btnCancelarProducto.setOnClickListener { findNavController().popBackStack() }

        return view
    }

    private fun cargarDatosBundle() {
        val bundle = arguments
        if (bundle != null) {
            productoId = bundle.getString("id")
            campoNombreProducto.setText(bundle.getString("nombre", ""))
            campoValorPorLibra.setText(bundle.getInt("precioLibra", 0).toString())
            campoDisponibilidad.setText(bundle.getInt("disponibilidad", 0).toString())

            val categoria = bundle.getString("categoria", "").capitalize()
            val posicionCategoria = (spinnerCategoria.adapter as ArrayAdapter<String>).getPosition(categoria)
            spinnerCategoria.setSelection(if (posicionCategoria >= 0) posicionCategoria else 0)

            imagenBase64 = bundle.getString("imagenBase64", "")
            if (imagenBase64.isNotBlank()) {
                val bytes = Base64.decode(imagenBase64, Base64.DEFAULT)
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imagenProducto.setImageBitmap(bmp)
            }
        }
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

    private fun guardarCambios() {
        val nombre = campoNombreProducto.text.toString().trim()
        val categoria = spinnerCategoria.selectedItem.toString()
        val valorPorLibra = campoValorPorLibra.text.toString().toIntOrNull()
        val disponibilidad = campoDisponibilidad.text.toString().toIntOrNull()

        // Obtener email vendedor desde SharedPreferences
        val prefs = requireContext().getSharedPreferences(Constantes.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val usuarioJson = prefs.getString("usuarioLogueado", null)
        val emailVendedor = if (usuarioJson != null) {
            val usuario = Gson().fromJson(usuarioJson, Usuario::class.java)
            usuario.correo
        } else {
            ""
        }

        if (productoId == null || nombre.isBlank() || imagenBase64.isBlank() || valorPorLibra == null || disponibilidad == null || emailVendedor.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Completa todos los campos correctamente.", Toast.LENGTH_SHORT).show()
            return
        }

        val productoActualizado = Producto(
            id = productoId!!,
            nombre = nombre,
            categoria = categoria,
            precioLibra = valorPorLibra,
            disponibilidad = disponibilidad,
            imagenBase64 = imagenBase64,
            vendedorEmail = emailVendedor!!
        )

        val exito = ProductoManager.actualizarProducto(requireContext(), productoActualizado)
        if (exito) {
            Toast.makeText(requireContext(), "Producto actualizado correctamente", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        } else {
            Toast.makeText(requireContext(), "Error al actualizar el producto", Toast.LENGTH_SHORT).show()
        }
    }
}
