package com.example.tpandroidapp.ui.Login

sealed class LoginIntent {
    data class EmailChanged(val email: String) : LoginIntent()
    data class PasswordChanged(val password: String) : LoginIntent()
    object SubmitLogin : LoginIntent()
}