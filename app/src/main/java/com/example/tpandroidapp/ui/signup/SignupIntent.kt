package com.example.tpandroidapp.ui.signup

sealed class SignupIntent {
    data class FullnameChanged(val fullname: String) : SignupIntent()
    data class EmailChanged(val email: String) : SignupIntent()
    data class PasswordChanged(val password: String) : SignupIntent()
    data class PhoneChanged(val phone: String) : SignupIntent()

    object SubmitSignup : SignupIntent()
}
