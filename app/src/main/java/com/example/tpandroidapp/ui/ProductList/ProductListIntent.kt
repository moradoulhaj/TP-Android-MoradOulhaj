package com.example.tpandroidapp.ui.ProductList

sealed class ProductListIntent {
    object LoadProducts : ProductListIntent()
    data class FilterByCategory(val category: String?) : ProductListIntent()
}


