package com.example.tpandroidapp.ui.Order

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpandroidapp.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    var state = mutableStateOf<OrderState>(OrderState.Loading)
        private set

    private val _eventFlow = Channel<UiEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun handleIntent(intent: OrderIntent) {
        when (intent) {
            is OrderIntent.LoadUserOrders -> loadUserOrders(intent.userId)
        }
    }

    private fun loadUserOrders(userId: Int) {
        state.value = OrderState.Loading
        viewModelScope.launch {
            try {
                val response = orderRepository.getUserOrders(userId)
                if (response.isSuccessful && response.body() != null) {
                    state.value = OrderState.Success(response.body()!!)
                } else {
                    state.value = OrderState.Error("Erreur : ${response.code()}")
                }
            } catch (e: Exception) {
                state.value = OrderState.Error("Erreur : ${e.localizedMessage ?: "Inconnue"}")
            }
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
    }
}
