package com.example.tpandroidapp.data.model

data class Product(
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val originalPrice: Double?,
    val promotionPercent: Int?,
    val category: String?,
    val img1: String?,
    val img2: String?,
    val img3: String?,
    val img4: String?,
    val createdAt: String?,
    val updatedAt: String?
)
