package com.example.tpandroidapp.ui.ProductDetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpandroidapp.data.repository.ProductRepository
import com.example.tpandroidapp.ui.ProductDetail.ProductDetailIntent
import com.example.tpandroidapp.ui.ProductDetail.ProductDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    var state = mutableStateOf<ProductDetailState>(ProductDetailState.Loading)
        private set

    fun handleIntent(intent: ProductDetailIntent) {
        when (intent) {
            is ProductDetailIntent.LoadProduct -> {
                loadProduct(intent.productId)
            }
        }
    }

    private fun loadProduct(productId: String) {
        state.value = ProductDetailState.Loading

        viewModelScope.launch {
            try {
                val response = repository.getProductById(productId)
                if (response.isSuccessful && response.body() != null) {
                    state.value = ProductDetailState.Success(response.body()!!)
                } else {
                    state.value = ProductDetailState.Error("Erreur : ${response.code()}")
                }
            } catch (e: Exception) {
                state.value = ProductDetailState.Error("Erreur : ${e.localizedMessage ?: "Inconnue"}")
            }
        }
    }
}
