package com.example.campomarket.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.campomarket.data.model.Usuario
import com.example.campomarket.util.Constantes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object UsuarioManager {

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constantes.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun guardarUsuarios(context: Context, usuarios: List<Usuario>) {
        val json = Gson().toJson(usuarios)
        getSharedPreferences(context).edit().putString(Constantes.KEY_USUARIOS, json).apply()
    }

    fun obtenerUsuarios(context: Context): MutableList<Usuario> {
        val json = getSharedPreferences(context).getString(Constantes.KEY_USUARIOS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Usuario>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    fun agregarUsuario(context: Context, usuario: Usuario): Boolean {
        val usuarios = obtenerUsuarios(context)
        if (usuarios.any { it.correo == usuario.correo }) return false // evitar duplicados
        usuarios.add(usuario)
        guardarUsuarios(context, usuarios)
        return true
    }

    fun buscarUsuarioPorCorreo(context: Context, correo: String): Usuario? {
        val usuarios = obtenerUsuarios(context)
        return usuarios.find { it.correo == correo }
    }

    fun actualizarUsuario(context: Context, usuarioActualizado: Usuario): Boolean {
        val usuarios = obtenerUsuarios(context)
        val index = usuarios.indexOfFirst { it.correo == usuarioActualizado.correo }
        if (index != -1) {
            usuarios[index] = usuarioActualizado
            guardarUsuarios(context, usuarios)
            return true
        }
        return false
    }

    fun obtenerUsuarioLogueado(context: Context): Usuario? {
        val json = getSharedPreferences(context).getString("usuarioLogueado", null)
        return if (json != null) Gson().fromJson(json, Usuario::class.java) else null
    }
}