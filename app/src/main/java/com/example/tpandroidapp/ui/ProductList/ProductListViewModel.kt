package com.example.tpandroidapp.ui.ProductList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpandroidapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = mutableStateOf<ProductListState>(ProductListState.Loading)
    val state: State<ProductListState> = _state

    fun handleIntent(intent: ProductListIntent) {
        when (intent) {
            is ProductListIntent.LoadProducts -> loadProducts()
        }
    }

    private fun loadProducts() {
        _state.value = ProductListState.Loading

        viewModelScope.launch {
            try {
                val response = repository.getAllProducts()
                if (response.isSuccessful && response.body() != null) {
                    val products = response.body()!!
                    if (products.isNotEmpty()) {
                        _state.value = ProductListState.Success(products)
                    } else {
                        _state.value = ProductListState.Error("Aucun produit trouvé")
                    }
                } else {
                    _state.value = ProductListState.Error("Erreur API : ${response.code()}")
                }
            } catch (e: Exception) {
                _state.value = ProductListState.Error("Erreur réseau : ${e.localizedMessage ?: "Inconnue"}")
            }
        }
    }
}
