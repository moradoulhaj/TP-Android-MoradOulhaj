package com.example.tpandroidapp.data.model

data class UserData(
    val id: Int,
    val fullname: String,
    val email: String,
    val phone: String,
    val admin: String,
    val createdAt: String,
    val updatedAt: String,
    val token: String? = null // token uniquement pour login
)
