package com.example.tpandroidapp.ui.ProductList

import com.example.tpandroidapp.model.Product


sealed class ProductListState {
    object Loading : ProductListState()
    data class Success(val products: List<Product>) : ProductListState()
    data class Error(val message: String) : ProductListState()
}
