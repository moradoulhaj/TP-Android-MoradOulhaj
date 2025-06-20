package com.example.tpandroidapp.data

import com.example.tpandroidapp.R
import com.example.tpandroidapp.data.model.Product
import com.example.tpandroidapp.data.network.ApiService
import retrofit2.Response

class ProductRepository(private val apiService: ApiService) {
        suspend fun getAllProducts(): Response<List<Product>> {
            return apiService.getProducts()
        }
    suspend fun getProductById(id: String): Product? {
        val response = apiService.getProductById(id)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    }
