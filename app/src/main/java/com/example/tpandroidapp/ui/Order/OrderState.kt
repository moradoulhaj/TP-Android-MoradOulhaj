package com.example.tpandroidapp.ui.Order

import com.example.tpandroidapp.data.model.Cart
import com.example.tpandroidapp.data.model.OrderResponse


sealed class OrderState {
    object Loading : OrderState()
    data class Success(val orders: List<OrderResponse>) : OrderState()
    data class Error(val message: String) : OrderState()
}

