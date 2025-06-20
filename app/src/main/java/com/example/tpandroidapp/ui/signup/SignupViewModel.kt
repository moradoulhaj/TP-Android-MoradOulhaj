package com.example.tpandroidapp.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {

    private val _state = MutableStateFlow(SignupState())
    val state: StateFlow<SignupState> = _state

    fun onIntent(intent: SignupIntent) {
        when (intent) {
            is SignupIntent.FullnameChanged -> {
                _state.value = _state.value.copy(fullname = intent.fullname)
            }
            is SignupIntent.EmailChanged -> {
                _state.value = _state.value.copy(email = intent.email)
            }
            is SignupIntent.PasswordChanged -> {
                _state.value = _state.value.copy(password = intent.password)
            }
            is SignupIntent.PhoneChanged -> {
                _state.value = _state.value.copy(phone = intent.phone)
            }
            SignupIntent.SubmitSignup -> {
                submitSignup()
            }
        }
    }

    private fun submitSignup() {
        val currentState = _state.value

        // Simple validation example
        if (currentState.fullname.isBlank() || currentState.email.isBlank() ||
            currentState.password.isBlank() || currentState.phone.isBlank()) {
            _state.value = currentState.copy(errorMessage = "Please fill all fields")
            return
        }

        _state.value = currentState.copy(isLoading = true, errorMessage = null)

        // Simulate network call
        viewModelScope.launch {
            // Here you would call your API

            // Simulate delay for demo
            kotlinx.coroutines.delay(1500)

            // Example success
            _state.value = currentState.copy(
                isLoading = false,
                isSuccess = true,
                errorMessage = null
            )
        }
    }
}
