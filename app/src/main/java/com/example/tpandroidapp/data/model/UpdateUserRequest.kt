package com.example.tpandroidapp.data.model

data class UpdateUserRequest(
    val fullname: String,
    val email: String,
    val password: String,
    val phone: String
)
