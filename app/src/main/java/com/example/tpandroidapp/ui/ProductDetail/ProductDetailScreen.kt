package com.example.tpandroidapp.ui.ProductDetail

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tpandroidapp.data.datastore.UserPreferences
import com.example.tpandroidapp.ui.utilsUI.CommentItem

@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ProductDetailScreen(
        productId: Int,
        navController: NavController,
        viewModel: ProductDetailViewModel = hiltViewModel()
    ) {
        val state by viewModel.state
        val comments by viewModel.comments

        var newComment by remember { mutableStateOf("") }
        var stars by remember { mutableStateOf(3) }
        var count by remember { mutableStateOf(1) }

        val context = LocalContext.current
        val userPreferences = remember { UserPreferences(context) }
        val userData by userPreferences.userFlow.collectAsState(initial = null)
        val userToken = userData?.token.orEmpty()
        val userId = userData?.id ?: 0
        val fullname = userData?.fullname ?: "Utilisateur"

        // Collect toast events from ViewModel
        LaunchedEffect(Unit) {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is ProductDetailViewModel.UiEvent.ShowToast -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

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
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF6200EE))
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(Color.White)
            ) {

                when (state) {
                    is ProductDetailState.Loading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF6200EE))
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
                                        .clip(RoundedCornerShape(12.dp))
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(product.name, style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(8.dp))

                            product.description?.let {
                                Text(it, style = MaterialTheme.typography.bodyMedium)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                "Prix : ${product.price} MAD",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF6200EE)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            product.category?.let {
                                Text("Catégorie : $it", style = MaterialTheme.typography.bodyMedium)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Quantity selector row
                            Text("Quantité :", style = MaterialTheme.typography.titleMedium)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { if (count > 1) count-- },
                                    enabled = count > 1
                                ) {
                                    Text("-", style = MaterialTheme.typography.titleLarge)
                                }
                                Text(count.toString(), modifier = Modifier.padding(horizontal = 8.dp))
                                IconButton(
                                    onClick = { count++ }
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Augmenter quantité") // <-- Add icon here
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Add to cart button
                            Button(
                                onClick = {
                                    if (userToken.isNotBlank()) {
                                        Log.d("ProductDetail", "Adding to cart: productId=${product.id}, count=$count, token=$userToken")

                                        viewModel.handleIntent(
                                            ProductDetailIntent.AddToCart(
                                                token = userToken,
                                                productId = product.id,
                                                count = count
                                            )
                                        )
                                    } else {
                                        Toast.makeText(context, "Veuillez vous connecter pour ajouter au panier.", Toast.LENGTH_SHORT).show()
                                        Log.w("ProductDetail", "Add to cart attempted without user token")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                            ) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Ajouter au Panier", color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(16.dp))

                            // Comments section
                            Text("Commentaires", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))

                            if (comments.isEmpty()) {
                                Text("Aucun commentaire pour ce produit.")
                            } else {
                                comments.forEach { comment ->
                                    CommentItem(comment)
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(16.dp))

                            // Add comment input
                            Text("Ajouter un commentaire", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))

                            BasicTextField(
                                value = newComment,
                                onValueChange = { newComment = it },
                                textStyle = TextStyle(color = Color.Black),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .background(Color(0xFFF3F3F3), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Star rating
                            Row {
                                for (i in 1..5) {
                                    Text(
                                        text = if (i <= stars) "⭐" else "☆",
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clickable { stars = i },
                                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            // Submit comment button
                            Button(
                                onClick = {
                                    if (userId != 0 && fullname.isNotBlank()) {
                                        viewModel.handleIntent(
                                            ProductDetailIntent.PostComment(
                                                productId = product.id,
                                                userId = userId,
                                                fullname = fullname,
                                                content = newComment,
                                                stars = List(stars) { true } + List(5 - stars) { false }
                                            )
                                        )
                                        newComment = ""
                                        stars = 3
                                    } else {
                                        Toast.makeText(context, "Veuillez vous connecter pour commenter.", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.align(Alignment.End),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                            ) {
                                Text("Envoyer", color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(24.dp))
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
