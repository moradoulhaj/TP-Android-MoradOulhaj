package com.example.tpandroidapp.ui.ProductList

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ProductListScreen(
    navController: NavController,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val state by viewModel.state

    // Load products only once when the screen enters composition
    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProductListIntent.LoadProducts)
    }

    when (state) {
        is ProductListState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Chargement des produits...")
            }
        }

        is ProductListState.Success -> {
            val products = (state as ProductListState.Success).products
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    ProductItem(product = product, navController = navController)
                }
            }
        }

        is ProductListState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Erreur : ${(state as ProductListState.Error).message}")
            }
        }
    }
}
