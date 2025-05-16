package com.example.campomarket.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.campomarket.R
import com.example.campomarket.data.model.Usuario
import com.example.campomarket.data.storage.UsuarioManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Log.d("Activity Splash", "OnCreate: Inicializando activity")

        // Crear usuario administrador si no existe
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val adminCreado = sharedPreferences.getBoolean("adminCreated", false)

        if (!adminCreado) crearUsuarioAdminPorDefecto(sharedPreferences)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivityAdmin::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    private fun crearUsuarioAdminPorDefecto(sharedPreferences: SharedPreferences) {
        val usuarioAdmin = Usuario(
            nombres = "Admin",
            apellidos = "CampoMarket",
            correo = "campomarketapp@gmail.com",
            tipoDocumento = "CC",
            numeroIdentificacion = "1234567890",
            celular = "3000000000",
            departamento = "AdminDepto",
            ciudad = "AdminCiudad",
            direccion = "AdminDireccion",
            rol = "administrador",
            clave = "admin123"
        )

        val creado = UsuarioManager.agregarUsuario(this, usuarioAdmin)

        if (creado) {
            Log.d("SplashActivity", "Usuario admin creado correctamente con UsuarioManager")
        } else {
            Log.d("SplashActivity", "El usuario admin ya existía")
        }

        sharedPreferences.edit().putBoolean("adminCreated", true).apply()
    }
}
