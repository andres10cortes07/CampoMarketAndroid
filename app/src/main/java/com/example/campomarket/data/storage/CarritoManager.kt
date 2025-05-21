package com.example.campomarket.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.campomarket.data.model.ItemCarrito
import com.example.campomarket.data.model.Producto
import com.example.campomarket.util.Constantes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CarritoManager {

    // Ahora cada usuario tendrá su propio archivo de preferencias para el carrito
    private fun getSharedPreferences(context: Context, userEmail: String): SharedPreferences {
        // Usamos el email del usuario para crear un nombre de archivo único para sus SharedPreferences
        val sanitizedEmail = userEmail.replace("@", "_").replace(".", "_")
        return context.getSharedPreferences("${Constantes.SHARED_PREF_CARRITO_NAME}_$sanitizedEmail", Context.MODE_PRIVATE)
    }

    // Todas las funciones ahora reciben userEmail
    fun guardarCarrito(context: Context, userEmail: String, carrito: List<ItemCarrito>) {
        val json = Gson().toJson(carrito)
        getSharedPreferences(context, userEmail).edit().putString(Constantes.KEY_CARRITO, json).apply()
    }

    fun obtenerCarrito(context: Context, userEmail: String): MutableList<ItemCarrito> {
        val json = getSharedPreferences(context, userEmail).getString(Constantes.KEY_CARRITO, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<ItemCarrito>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    fun agregarProductoAlCarrito(context: Context, userEmail: String, producto: Producto) {
        val carrito = obtenerCarrito(context, userEmail)
        val itemExistente = carrito.find { it.producto.id == producto.id }

        if (itemExistente != null) {
            itemExistente.cantidad++
        } else {
            carrito.add(ItemCarrito(producto, 1))
        }
        guardarCarrito(context, userEmail, carrito)
    }

    fun eliminarProductoDelCarrito(context: Context, userEmail: String, productId: String) {
        val carrito = obtenerCarrito(context, userEmail)
        val updatedCarrito = carrito.filter { it.producto.id != productId }.toMutableList()
        guardarCarrito(context, userEmail, updatedCarrito)
    }

    fun actualizarCantidadProducto(context: Context, userEmail: String, productId: String, newCantidad: Int) {
        val carrito = obtenerCarrito(context, userEmail)
        val item = carrito.find { it.producto.id == productId }
        item?.let {
            it.cantidad = newCantidad
            guardarCarrito(context, userEmail, carrito)
        }
    }

    fun limpiarCarrito(context: Context, userEmail: String) {
        getSharedPreferences(context, userEmail).edit().remove(Constantes.KEY_CARRITO).apply()
    }
}