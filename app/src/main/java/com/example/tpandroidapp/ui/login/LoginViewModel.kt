import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpandroidapp.data.network.RetrofitClient
import com.example.tpandroidapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UserRepository = UserRepository(RetrofitClient.apiService)
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    var onLoginSuccess: (() -> Unit)? = null

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> _state.value = _state.value.copy(email = intent.email)
            is LoginIntent.PasswordChanged -> _state.value = _state.value.copy(password = intent.password)
            is LoginIntent.SubmitLogin -> submitLogin()
        }
    }

    private fun handleLoginSuccess() {
        _state.value = _state.value.copy(
            isLoading = false,
            isSuccess = true,
            errorMessage = null
        )
        onLoginSuccess?.invoke()
    }

    private fun submitLogin() {
        val currentState = _state.value

        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _state.value = currentState.copy(errorMessage = "Email and password must not be empty")
            return
        }

        _state.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val response = repository.login(currentState.email, currentState.password)
                if (response.isSuccessful && response.body() != null) {
                    // Met à jour l'état avant d'appeler le callback
                    _state.value = currentState.copy(isLoading = false, isSuccess = true, errorMessage = null)
                    handleLoginSuccess() // <-- Ici on déclenche le callback
                } else {
                    _state.value = currentState.copy(
                        isLoading = false,
                        errorMessage = "Login failed: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _state.value = currentState.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Unknown error"
                )
            }
        }
    }
}
