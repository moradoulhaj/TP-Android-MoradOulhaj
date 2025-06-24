package com.example.tpandroidapp.ui.ProductDetail


sealed class ProductDetailIntent {
    data class LoadProduct(val productId: Int) : ProductDetailIntent()
    data class PostComment(
        val productId: Int,
        val userId: Int,
        val fullname: String,
        val content: String,
        val stars: List<Boolean>
    ) : ProductDetailIntent()

    data class AddToCart(val token: String, val productId: Int, val count: Int) : ProductDetailIntent()

}
