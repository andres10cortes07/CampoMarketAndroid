package com.example.campomarket.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.campomarket.data.model.Compra
import com.example.campomarket.data.model.ItemCarrito
import com.example.campomarket.util.Constantes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date
import java.util.UUID

object CompraManager {

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constantes.SHARED_PREF_COMPRAS_NAME, Context.MODE_PRIVATE)
    }

    fun guardarCompras(context: Context, compras: List<Compra>) {
        val json = Gson().toJson(compras)
        getSharedPreferences(context).edit().putString(Constantes.KEY_COMPRAS, json).apply()
    }

    fun obtenerTodasLasCompras(context: Context): MutableList<Compra> {
        val json = getSharedPreferences(context).getString(Constantes.KEY_COMPRAS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Compra>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    fun agregarCompra(context: Context, compra: Compra) {
        val compras = obtenerTodasLasCompras(context)
        compras.add(compra)
        guardarCompras(context, compras)
    }

    fun obtenerComprasPorUsuario(context: Context, userEmail: String): List<Compra> {
        return obtenerTodasLasCompras(context).filter { it.emailComprador == userEmail }
    }

    fun registrarCompra(context: Context, userEmail: String, itemsCarrito: List<ItemCarrito>): Compra {
        val totalCompra = itemsCarrito.sumOf { it.producto.precioLibra * it.cantidad }
        val nuevaCompra = Compra(
            id = UUID.randomUUID().toString(),
            fechaCompra = Date(),
            emailComprador = userEmail,
            items = itemsCarrito,
            total = totalCompra
        )
        agregarCompra(context, nuevaCompra)
        return nuevaCompra
    }
}