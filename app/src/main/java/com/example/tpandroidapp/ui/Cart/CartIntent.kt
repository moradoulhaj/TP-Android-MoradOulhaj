package com.example.tpandroidapp.ui.Cart


    sealed class CartIntent {
        data class LoadCart(val token: String) : CartIntent()
        data class PlaceOrder(val token: String, val address: String) : CartIntent()
    }
