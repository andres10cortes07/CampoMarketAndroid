package com.example.campomarket.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.campomarket.data.model.Producto
import com.example.campomarket.util.Constantes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ProductoManager {

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constantes.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun guardarProductos(context: Context, productos: List<Producto>) {
        val json = Gson().toJson(productos)
        getSharedPreferences(context).edit().putString(Constantes.KEY_PRODUCTOS, json).apply()
    }

    fun obtenerProductos(context: Context): MutableList<Producto> {
        val json = getSharedPreferences(context).getString(Constantes.KEY_PRODUCTOS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Producto>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    fun agregarProducto(context: Context, producto: Producto): Boolean {
        val productos = obtenerProductos(context)
        if (productos.any { it.id == producto.id }) return false // evitar duplicados
        productos.add(producto)
        guardarProductos(context, productos)
        return true
    }

    fun actualizarProducto(context: Context, productoActualizado: Producto): Boolean {
        val productos = obtenerProductos(context)
        val index = productos.indexOfFirst { it.id == productoActualizado.id }
        if (index != -1) {
            productos[index] = productoActualizado
            guardarProductos(context, productos)
            return true
        }
        return false
    }

    fun eliminarProducto(context: Context, id: String): Boolean {
        val productos = obtenerProductos(context)
        val nuevos = productos.filter { it.id != id }
        if (nuevos.size != productos.size) {
            guardarProductos(context, nuevos)
            return true
        }
        return false
    }

    fun obtenerPorVendedor(context: Context, email: String): List<Producto> {
        return obtenerProductos(context).filter { it.vendedorEmail == email }
    }

    fun obtenerPorCategoria(context: Context, categoria: String): List<Producto> {
        return obtenerProductos(context).filter { it.categoria.equals(categoria, ignoreCase = true) }
    }
}