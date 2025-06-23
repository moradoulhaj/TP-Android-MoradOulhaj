package com.example.tpandroidapp.ui.ProductDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tpandroidapp.data.model.Comment
import com.example.tpandroidapp.ui.utils.CommentItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavController,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val comments by viewModel.comments

    // Load product + comments
    LaunchedEffect(productId) {
        viewModel.handleIntent(ProductDetailIntent.LoadProduct(productId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Détails du produit") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (state) {
                is ProductDetailState.Loading -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ProductDetailState.Success -> {
                    val product = (state as ProductDetailState.Success).product
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        product.img1?.let { imgUrl ->
                            Image(
                                painter = rememberAsyncImagePainter(imgUrl),
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        Text(text = product.name, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))

                        product.description?.let {
                            Text(text = it, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Text(
                            text = "Prix : ${product.price} MAD",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        product.category?.let {
                            Text(text = "Catégorie : $it", style = MaterialTheme.typography.bodyMedium)
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))

                        // 🔽 Commentaires
                        Text("Commentaires", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        if (comments.isEmpty()) {
                            Text("Aucun commentaire pour ce produit.")
                        } else {
                            comments.forEach { comment ->
                                CommentItem(comment)
                            }
                        }
                    }
                }

                is ProductDetailState.Error -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Erreur : ${(state as ProductDetailState.Error).message}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}