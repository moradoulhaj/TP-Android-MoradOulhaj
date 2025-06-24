package com.example.tpandroidapp.ui.Profile
sealed class ProfileIntent {
    object LoadUser : ProfileIntent()
    object Logout : ProfileIntent()
    data class UpdateUser(
        val fullname: String,
        val email: String,
        val phone: String,
        val password: String?,
        val confirmPassword: String?
    ) : ProfileIntent()
}
