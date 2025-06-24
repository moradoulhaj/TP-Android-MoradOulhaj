package com.example.tpandroidapp.data.network

import com.example.tpandroidapp.data.model.Cart
import com.example.tpandroidapp.data.model.Comment
import com.example.tpandroidapp.data.model.CommentRequest
import com.example.tpandroidapp.data.model.Product
import com.example.tpandroidapp.data.model.UserCredentials
import com.example.tpandroidapp.data.model.UserData
import com.example.tpandroidapp.data.model.UserRegister
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    //Authentification api
    @POST("api/v1/users/login")
    suspend fun login(@Body credentials: UserCredentials): Response<UserData>

    @POST("api/v1/users/register")
    suspend fun register(@Body user: UserRegister): Response<UserData>
    //Products api
    @GET("/api/v1/products")
    suspend fun getProducts(): Response<List<Product>>
    @GET("/api/v1/products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>
    // Comments
    @GET("api/v1/comments")
    suspend fun getCommentsByProduct(@Query("idProduct") productId: Int): Response<List<Comment>>
    @POST("api/v1/comments")
    suspend fun createComment(@Body comment: CommentRequest): Response<Comment>
    //Cart api
    @GET("/api/v1/carts")
    suspend fun getCartItems(
        @Header("token") token: String
    ): Response<List<Cart>>

    @POST("/api/v1/carts")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Query("idProduct") idProduct: Int,
        @Query("productCount") productCount: Int
    ): Response<Cart>


}
