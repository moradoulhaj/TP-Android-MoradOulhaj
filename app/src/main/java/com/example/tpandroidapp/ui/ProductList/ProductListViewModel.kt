package com.example.tpandroidapp.ui.ProductList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpandroidapp.data.ProductRepository
import com.example.tpandroidapp.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val repository: ProductRepository = ProductRepository(RetrofitClient.apiService)
) : ViewModel() {

    private val _state = MutableStateFlow(ProductListState())
    val state: StateFlow<ProductListState> = _state

    fun onIntent(intent: ProductListIntent) {
        when (intent) {
            is ProductListIntent.LoadProducts -> loadProducts()
            is ProductListIntent.FilterByCategory -> filterCategory(intent.category)
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = repository.getAllProducts()
                if (response.isSuccessful && response.body() != null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        products = response.body() ?: emptyList()
                    )
                } else {
                    _state.value = _state.value.copy(isLoading = false, errorMessage = "Erreur de chargement")
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, errorMessage = e.localizedMessage)
            }
        }
    }

    private fun filterCategory(category: String?) {
        _state.value = _state.value.copy(selectedCategory = category)
    }
}
