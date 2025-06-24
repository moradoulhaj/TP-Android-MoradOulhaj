package com.example.tpandroidapp.ui.Order




sealed class OrderIntent {
    data class LoadUserOrders(val userId: Int) : OrderIntent()
}

