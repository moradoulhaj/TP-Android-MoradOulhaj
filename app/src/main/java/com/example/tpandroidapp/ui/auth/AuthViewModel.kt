package com.example.tpandroidapp.ui.auth


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    // Etat de connexion (false = pas connecté, true = connecté)
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    // Simule la connexion : appeler après login réussi
    fun login() {
        _isLoggedIn.value = true
    }

    // Simule la déconnexion
    fun logout() {
        _isLoggedIn.value = false
    }
}
