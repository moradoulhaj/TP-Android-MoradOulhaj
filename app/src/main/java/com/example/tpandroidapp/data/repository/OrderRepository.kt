package com.example.tpandroidapp.data.repository

import com.example.tpandroidapp.data.model.OrderRequest
import com.example.tpandroidapp.data.model.OrderResponse
import com.example.tpandroidapp.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun placeOrder(orderRequest: OrderRequest): Response<OrderResponse> {
        return apiService.placeOrder(orderRequest)
    }

    suspend fun getUserOrders(userId: String): Response<List<OrderResponse>> {
        return apiService.getUserOrders(userId)
    }
}
