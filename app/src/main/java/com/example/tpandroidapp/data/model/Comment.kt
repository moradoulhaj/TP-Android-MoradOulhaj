package com.example.tpandroidapp.data.model

data class Comment(
    val id: Int,
    val idProduct: Int,
    val idUser: Int,
    val fullname: String,
    val content: String,
    val star1: String,
    val star2: String,
    val star3: String,
    val star4: String,
    val star5: String,
    val createdAt: String,
    val updatedAt: String
)
data class CommentRequest(
    val idProduct: Int,
    val idUser: Int,
    val fullname: String,
    val content: String,
    val star1: String,
    val star2: String,
    val star3: String,
    val star4: String,
    val star5: String
)

