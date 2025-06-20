package com.example.tpandroidapp.ui.ProductList

import com.example.tpandroidapp.data.model.Product


data class ProductListState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val selectedCategory: String? = null,
    val errorMessage: String? = null
)
