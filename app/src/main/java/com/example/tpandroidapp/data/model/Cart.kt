package com.example.tpandroidapp.data.model

data class Cart(
    val id: Int,
    val idUser: Int,
    val idProduct: Int,
    val nameProduct: String,
    val priceProduct: Int,
    val count: Int,
    val img: String,
    val createdAt: String,
    val updatedAt: String
)
