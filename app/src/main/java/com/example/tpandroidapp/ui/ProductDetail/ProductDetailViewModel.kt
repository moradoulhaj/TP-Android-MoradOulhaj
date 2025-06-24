package com.example.tpandroidapp.ui.ProductDetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpandroidapp.data.model.Comment
import com.example.tpandroidapp.data.model.CommentRequest
import com.example.tpandroidapp.data.repository.CartRepository
import com.example.tpandroidapp.data.repository.CommentRepository
import com.example.tpandroidapp.data.repository.ProductRepository
import com.example.tpandroidapp.ui.ProductDetail.ProductDetailIntent
import com.example.tpandroidapp.ui.ProductDetail.ProductDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel
@Inject constructor(
    private val productRepository: ProductRepository,
    private val commentRepository: CommentRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    var state = mutableStateOf<ProductDetailState>(ProductDetailState.Loading)
        private set

    var comments = mutableStateOf<List<Comment>>(emptyList())
        private set

    // SharedFlow for UI events like Toast
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
    }

    fun handleIntent(intent: ProductDetailIntent) {
        when (intent) {
            is ProductDetailIntent.LoadProduct -> {
                loadProduct(intent.productId)
                loadComments(intent.productId.toInt())
            }
            is ProductDetailIntent.PostComment -> postComment(intent)
            is ProductDetailIntent.AddToCart -> addToCart(intent)
        }
    }

    private fun postComment(intent: ProductDetailIntent.PostComment) {
        val newComment = Comment(
            id = -1, // temporary local
            idProduct = intent.productId,
            idUser = intent.userId,
            fullname = intent.fullname,
            content = intent.content,
            star1 = intent.stars.getOrNull(0)?.toString() ?: "false",
            star2 = intent.stars.getOrNull(1)?.toString() ?: "false",
            star3 = intent.stars.getOrNull(2)?.toString() ?: "false",
            star4 = intent.stars.getOrNull(3)?.toString() ?: "false",
            star5 = intent.stars.getOrNull(4)?.toString() ?: "false",
            createdAt = "", updatedAt = ""
        )

        comments.value = comments.value + newComment // Add locally

        viewModelScope.launch {
            try {
                val request = CommentRequest(
                    idProduct = intent.productId,
                    idUser = intent.userId,
                    fullname = intent.fullname,
                    content = intent.content,
                    star1 = newComment.star1,
                    star2 = newComment.star2,
                    star3 = newComment.star3,
                    star4 = newComment.star4,
                    star5 = newComment.star5
                )
                commentRepository.postComment(request)
            } catch (_: Exception) {
                // Ignore failures (already added locally)
            }
        }
    }

    private fun addToCart(intent: ProductDetailIntent.AddToCart) {
        viewModelScope.launch {
            try {
                val response = cartRepository.addProductToCart(intent.token, intent.productId, intent.count)
                if (response.isSuccessful) {
                    _eventFlow.emit(UiEvent.ShowToast("Produit ajouté au panier avec succès"))
                } else {
                    _eventFlow.emit(UiEvent.ShowToast("Erreur lors de l'ajout au panier : ${response.code()}"))
                }
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowToast("Une erreur s'est produite :${e}"))
            }
        }
    }

    private fun loadProduct(productId: Int) {
        state.value = ProductDetailState.Loading

        viewModelScope.launch {
            try {
                val response = productRepository.getProductById(productId)
                if (response.isSuccessful && response.body() != null) {
                    state.value = ProductDetailState.Success(response.body()!!)
                } else {
                    state.value = ProductDetailState.Error("Erreur : ${response.code()}")
                }
            } catch (e: Exception) {
                state.value = ProductDetailState.Error("Erreur : ${e.localizedMessage ?: "Inconnue"}")
            }
        }
    }

    private fun loadComments(productId: Int) {
        viewModelScope.launch {
            try {
                val response = commentRepository.getCommentsByProduct(productId)
                if (response.isSuccessful && response.body() != null) {
                    comments.value = response.body()!!
                }
            } catch (e: Exception) {
                comments.value = emptyList()
            }
        }
    }
}
