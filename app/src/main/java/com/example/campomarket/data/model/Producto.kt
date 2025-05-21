package com.example.campomarket.data.model

data class Producto(
    val id: String,
    val nombre: String,
    val categoria: String,
    val precioLibra: Int,
    val disponibilidad : Int,
    val imagenBase64: String,
    val vendedorEmail: String
)