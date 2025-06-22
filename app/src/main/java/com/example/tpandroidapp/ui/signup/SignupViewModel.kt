package com.example.tpandroidapp.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpandroidapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

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
                val response = repository.register(
                    fullname = currentState.fullname,
                    email = currentState.email,
                    password = currentState.password,
                    phone = currentState.phone
                )

                if (response.isSuccessful && response.body() != null) {
                    _state.value = currentState.copy(
                        isLoading = false,
                        isSuccess = true,
                        errorMessage = null
                    )
                } else {
                    _state.value = currentState.copy(
                        isLoading = false,
                        errorMessage = "Signup failed: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                _state.value = currentState.copy(
                    isLoading = false,
                    errorMessage = "Signup error: ${e.localizedMessage ?: "Unknown error"}"
                )
            }
        }
    }
}
