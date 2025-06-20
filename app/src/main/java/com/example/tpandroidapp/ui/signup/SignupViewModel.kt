package com.example.tpandroidapp.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpandroidapp.data.model.UserRegister
import com.example.tpandroidapp.data.network.RetrofitClient.apiService
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

        if (currentState.fullname.isBlank() || currentState.email.isBlank() ||
            currentState.password.isBlank() || currentState.phone.isBlank()) {
            _state.value = currentState.copy(errorMessage = "Please fill all fields")
            return
        }

        _state.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val request = UserRegister(
                    fullname = currentState.fullname,
                    email = currentState.email,
                    password = currentState.password,
                    phone = currentState.phone
                )

                val response = apiService.register(request)

                if (response.isSuccessful && response.body() != null) {
                    // Signup successful
                    _state.value = currentState.copy(
                        isLoading = false,
                        isSuccess = true,
                        errorMessage = null
                    )
                } else {
                    // API returned error
                    _state.value = currentState.copy(
                        isLoading = false,
                        errorMessage = "Signup failed: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                // Network or other error
                _state.value = currentState.copy(
                    isLoading = false,
                    errorMessage = "Signup error: ${e.localizedMessage ?: "Unknown error"}"
                )
            }
        }
    }
}
