package com.example.tpandroidapp.data.repository

import com.example.tpandroidapp.data.model.Cart
import com.example.tpandroidapp.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getCart(token: String): Response<List<Cart>> {
        return apiService.getCartItems("Bearer $token")
    }

    suspend fun addProductToCart(token: String, productId: Int, count: Int): Response<Cart> {
        return apiService.addToCart("Bearer $token", idProduct = productId, productCount = count)
    }
}
