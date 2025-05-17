package com.example.tpandroidapp.ui.ProductDetail


sealed class ProductDetailIntent {
    data class LoadProduct(val productId: String) : ProductDetailIntent()
}
