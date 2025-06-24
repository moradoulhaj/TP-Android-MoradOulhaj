package com.example.tpandroidapp.ui.Profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpandroidapp.data.datastore.UserPreferences
import com.example.tpandroidapp.data.model.UpdateUserRequest
import com.example.tpandroidapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val userRepository: UserRepository
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
            is ProfileIntent.UpdateUser -> updateUser(intent)
        }
    }

    private fun updateUser(intent: ProfileIntent.UpdateUser) {
        viewModelScope.launch {
            if (!intent.password.isNullOrBlank() || !intent.confirmPassword.isNullOrBlank()) {
                if (intent.password != intent.confirmPassword) {
                    _eventFlow.emit(UiEvent.ShowToast("Les mots de passe ne correspondent pas"))
                    return@launch
                }
            }

            val userData = userPreferences.userFlow.firstOrNull()
            if (userData == null) {
                _eventFlow.emit(UiEvent.ShowToast("Utilisateur non connecté."))
                return@launch
            }

            val updateRequest = UpdateUserRequest(
                fullname = intent.fullname,
                email = intent.email,
                phone = intent.phone,
                password = intent.password ?: "" // Empty string if not changing
            )

            try {
                val response = userRepository.updateUser(userData.id, updateRequest)
                if (response.isSuccessful) {
                    _eventFlow.emit(UiEvent.ShowToast("Informations mises à jour avec succès"))

                    // Optional: update saved user data if server returns it
                    userPreferences.saveUser(userData.copy(
                        fullname = intent.fullname,
                        email = intent.email,
                        phone = intent.phone
                    ))

                    handleIntent(ProfileIntent.LoadUser)
                } else {
                    _eventFlow.emit(UiEvent.ShowToast("Échec de la mise à jour"))
                }
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowToast("Erreur lors de la mise à jour : ${e.message}"))
            }
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
