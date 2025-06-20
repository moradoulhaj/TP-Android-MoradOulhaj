package com.example.tpandroidapp.data.network

import com.example.tpandroidapp.data.model.Product
import com.example.tpandroidapp.data.model.UserCredentials
import com.example.tpandroidapp.data.model.UserData
import com.example.tpandroidapp.data.model.UserRegister
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("api/v1/users/login")
    suspend fun login(@Body credentials: UserCredentials): Response<UserData>

    @POST("api/v1/users/register")
    suspend fun register(@Body user: UserRegister): Response<UserData>

    @GET("/api/v1/products")
    suspend fun getProducts(): Response<List<Product>>
    @GET("/api/v1/products/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<Product>
}
