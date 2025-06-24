package com.example.tpandroidapp.data.model

data class OrderRequest(
    val idUser: String,
    val phone: String,
    val address: String,
    val fullname: String,
    val total: Int,
    val cart: Int // just a cart ID (for test)
)


data class CartItem(
    val id: String,
    val nameProduct: String,
    val priceProduct: Double,
    val count: Int,
    val img: String
)

data class OrderResponse(
    val status: Boolean,
    val delivery: Boolean,
    val id: Int,
    val idUser: String,
    val phone: String,
    val address: String,
    val cart: Int,
    val fullname: String,
    val total: Double,
    val updatedAt: String,
    val createdAt: String
)

