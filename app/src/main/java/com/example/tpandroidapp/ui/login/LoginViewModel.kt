import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> _state.value = _state.value.copy(email = intent.email)
            is LoginIntent.PasswordChanged -> _state.value = _state.value.copy(password = intent.password)
            is LoginIntent.SubmitLogin -> submitLogin()
        }
    }

    private fun submitLogin() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            // Fake delay or logic
            kotlinx.coroutines.delay(1500)
            if (_state.value.email == "test@example.com" && _state.value.password == "password") {
                _state.value = _state.value.copy(isLoading = false, isSuccess = true)
            } else {
                _state.value = _state.value.copy(isLoading = false, errorMessage = "Invalid credentials")
            }
        }
    }
}
