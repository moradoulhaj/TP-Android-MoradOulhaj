package com.example.tpandroidapp.ui.ProductList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tpandroidapp.data.ProductRepository

class ProductListViewModel : ViewModel() {

    private val _state = mutableStateOf<ProductListState>(ProductListState.Loading)
    val state: State<ProductListState> = _state

    fun handleIntent(intent: ProductListIntent) {
        when (intent) {
            is ProductListIntent.LoadProducts -> loadProducts()
        }
    }

    private fun loadProducts() {
        val products = ProductRepository.productList
        if (products.isNotEmpty()) {
            _state.value = ProductListState.Success(products)
        } else {
            _state.value = ProductListState.Error("Aucun produit trouvé")
        }
    }
}
