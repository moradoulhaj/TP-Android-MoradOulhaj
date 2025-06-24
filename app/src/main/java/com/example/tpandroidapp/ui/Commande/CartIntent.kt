package com.example.tpandroidapp.ui.Commande

import com.example.tpandroidapp.data.model.OrderRequest


sealed class CartIntent {
        data class LoadCart(val token: String) : CartIntent()
        data class PlaceOrder(
            val orderRequest: OrderRequest
        ) : CartIntent()    }
