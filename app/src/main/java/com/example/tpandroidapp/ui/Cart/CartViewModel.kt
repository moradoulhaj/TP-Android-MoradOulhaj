package com.example.tpandroidapp.ui.Cart

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpandroidapp.data.model.Cart
import com.example.tpandroidapp.data.repository.CartRepository
import com.example.tpandroidapp.ui.Cart.CartIntent
import com.example.tpandroidapp.ui.Cart.CartState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    var state = mutableStateOf<CartState>(CartState.Loading)
        private set

    private val _eventFlow = Channel<UiEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun handleIntent(intent: CartIntent) {
        when (intent) {
            is CartIntent.LoadCart -> loadCart(intent.token)
            is CartIntent.PlaceOrder -> placeOrder(intent.token, intent.address)
        }
    }

    private fun loadCart(token: String) {
        state.value = CartState.Loading
        viewModelScope.launch {
            try {
                val response = cartRepository.getCart(token)
                if (response.isSuccessful && response.body() != null) {
                    state.value = CartState.Success(response.body()!!)
                } else {
                    state.value = CartState.Error("Erreur : ${response.code()}")
                }
            } catch (e: Exception) {
                state.value = CartState.Error("Erreur : ${e.localizedMessage ?: "Inconnue"}")
            }
        }
    }

    private fun placeOrder(token: String, address: String) {
        viewModelScope.launch {
            try {
                // TODO: call your order placing API here
                // For demo, just simulate success
                _eventFlow.send(UiEvent.ShowToast("Commande validée avec succès !"))
                state.value = CartState.OrderPlaced
            } catch (e: Exception) {
                _eventFlow.send(UiEvent.ShowToast("Erreur lors de la validation de la commande"))
            }
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
    }
}
