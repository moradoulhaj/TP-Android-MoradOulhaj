package com.example.tpandroidapp.data.repository

import com.example.tpandroidapp.R
import com.example.tpandroidapp.data.model.Product


import com.example.tpandroidapp.data.network.ApiService
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAllProducts() = apiService.getProducts()

    suspend fun getProductById(id: String) = apiService.getProductById(id)
}

