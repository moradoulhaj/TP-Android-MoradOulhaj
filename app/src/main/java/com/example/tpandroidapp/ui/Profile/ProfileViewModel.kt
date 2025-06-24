package com.example.tpandroidapp.ui.Profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpandroidapp.data.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _state = mutableStateOf<ProfileState>(ProfileState.Loading)
    val state = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
        object NavigateToLogin : UiEvent() // Added this event for navigation
    }

    fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.LoadUser -> loadUser()
            ProfileIntent.Logout -> logout()
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            userPreferences.userFlow.collect { userData ->
                if (userData == null) {
                    _state.value = ProfileState.Error("Aucun utilisateur connecté.")
                } else {
                    _state.value = ProfileState.Success(user = userData)
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            userPreferences.clearUser()
            _eventFlow.emit(UiEvent.ShowToast("Déconnexion réussie"))
            _eventFlow.emit(UiEvent.NavigateToLogin)  // Emit navigation event here
            _state.value = ProfileState.LoggedOut
        }
    }
}
