package com.example.tpandroidapp.ui.ProductDetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tpandroidapp.data.ProductRepository
import com.example.tpandroidapp.model.Product

class ProductDetailViewModel : ViewModel() {

    var state = mutableStateOf<ProductDetailState>(ProductDetailState.Loading)
        private set

    fun handleIntent(intent: ProductDetailIntent) {
        when (intent) {
            is ProductDetailIntent.LoadProduct -> {
                val product = ProductRepository.getProductById(intent.productId)
                if (product != null) {
                    state.value = ProductDetailState.Success(product)
                } else {
                    state.value = ProductDetailState.Error("Produit introuvable")
                }
            }
        }
    }
}
