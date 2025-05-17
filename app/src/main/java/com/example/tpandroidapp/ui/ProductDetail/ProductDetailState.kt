package com.example.tpandroidapp.ui.ProductDetail


import com.example.tpandroidapp.model.Product

sealed class ProductDetailState {
    object Loading : ProductDetailState()
    data class Success(val product: Product) : ProductDetailState()
    data class Error(val message: String) : ProductDetailState()
}
