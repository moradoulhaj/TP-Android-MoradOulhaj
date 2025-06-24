package com.example.tpandroidapp.data.repository


import com.example.tpandroidapp.data.model.UpdateUserRequest
import com.example.tpandroidapp.data.model.UserCredentials
import com.example.tpandroidapp.data.model.UserData
import com.example.tpandroidapp.data.model.UserRegister
import com.example.tpandroidapp.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(email: String, password: String) =
        apiService.login(UserCredentials(email, password))

    suspend fun register(fullname: String, email: String, password: String, phone: String) =
        apiService.register(UserRegister(fullname, email, password, phone))
    suspend fun updateUser(userId: Int, updateUserRequest: UpdateUserRequest): Response<UpdateUserRequest> {
        return apiService.updateUser(userId, updateUserRequest)
    }
}

