package com.example.tpandroidapp.data.repository

import com.example.tpandroidapp.data.model.Comment
import com.example.tpandroidapp.data.model.CommentRequest
import com.example.tpandroidapp.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val apiService: ApiService
) {
    // Fetch all comments for a product
    suspend fun getCommentsByProduct(productId: Int): Response<List<Comment>> {
        return apiService.getCommentsByProduct(productId)
    }

    // Post a new comment
    suspend fun postComment(comment: CommentRequest): Response<Comment> {
        return apiService.createComment(comment)
    }
}
