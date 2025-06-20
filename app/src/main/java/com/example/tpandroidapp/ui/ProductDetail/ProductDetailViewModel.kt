package com.example.tpandroidapp.ui.ProductDetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpandroidapp.data.ProductRepository
import com.example.tpandroidapp.data.network.RetrofitClient
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {

    var state = mutableStateOf<ProductDetailState>(ProductDetailState.Loading)
        private set

    private val repository = ProductRepository(RetrofitClient.apiService)

    fun handleIntent(intent: ProductDetailIntent) {
        when (intent) {
            is ProductDetailIntent.LoadProduct -> {
                state.value = ProductDetailState.Loading
                viewModelScope.launch {
                    try {
                        val product = repository.getProductById(intent.productId)
                        state.value = if (product != null) {
                            ProductDetailState.Success(product)
                        } else {
                            ProductDetailState.Error("Produit introuvable")
                        }
                    } catch (e: Exception) {
                        state.value = ProductDetailState.Error(e.localizedMessage ?: "Erreur réseau")
                    }
                }
            }
        }
    }
}
