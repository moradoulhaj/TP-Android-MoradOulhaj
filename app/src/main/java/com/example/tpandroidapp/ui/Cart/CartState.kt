package com.example.tpandroidapp.ui.Cart

import com.example.tpandroidapp.data.model.Cart


sealed class CartState {
    object Loading : CartState()
    data class Success(val items: List<Cart>) : CartState()
    data class Error(val message: String) : CartState()
    object OrderPlaced : CartState()
}
