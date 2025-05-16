package com.example.campomarket.data.model

data class Usuario(
    val nombres: String,
    val apellidos: String,
    val tipoDocumento: String,
    val numeroIdentificacion: String,
    val correo: String,
    val celular: String,
    val departamento: String,
    val ciudad: String,
    val direccion: String,
    val rol: String, // "Administrador", "Vendedor", "Comprador"
    val clave: String
)