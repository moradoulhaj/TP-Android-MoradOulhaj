package com.example.tpandroidapp.data.repository

import com.example.tpandroidapp.data.model.UserCredentials
import com.example.tpandroidapp.data.model.UserRegister
import com.example.tpandroidapp.data.network.ApiService

class UserRepository(private val apiService: ApiService) {

    suspend fun login(email: String, password: String) = apiService.login(
        UserCredentials(email, password)
    )

    suspend fun register(fullname: String, email: String, password: String, phone: String) = apiService.register(
        UserRegister(fullname, email, password, phone)
    )
}
