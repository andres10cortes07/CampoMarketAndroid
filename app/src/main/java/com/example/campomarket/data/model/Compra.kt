package com.example.campomarket.data.model

import java.util.Date

data class Compra(
    val id: String,
    val fechaCompra: Date,
    val emailComprador: String,
    val items: List<ItemCarrito>,
    val total: Int
)