package com.example.tpandroidapp.ui.ProductList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tpandroidapp.data.model.Product
import com.example.tpandroidapp.ui.utils.ProductCardWithCarousel

@Composable
fun ProductListScreen(
    navController: NavController,
    viewModel: ProductListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProductListIntent.LoadProducts)
    }

    when (state) {
        is ProductListState.Loading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ProductListState.Error -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "Erreur : ${(state as ProductListState.Error).message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is ProductListState.Success -> {
            val products = (state as ProductListState.Success).products
            val newArrivals = products.take(6)
            val bestDeals = products.filter { it.promotionPercent ?: 0 > 0 }.take(6)
            val categories = products.mapNotNull { it.category }.distinct()
            val filtered = products.filter { selectedCategory == null || it.category == selectedCategory }

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // New Arrivals
                item {
                    SectionHeader("Nouveautés")
                    ProductCarousel(newArrivals, navController)
                }

                // Best Deals
                item {
                    SectionHeader("Meilleures Offres")
                    ProductCarousel(bestDeals, navController)
                }

                // Category Filters
                item {
                    SectionHeader("Filtrer par Catégorie")
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedCategory == null,
                                onClick = { selectedCategory = null },
                                label = { Text("Tous") }
                            )
                        }
                        items(categories) { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = category },
                                label = { Text(category) }
                            )
                        }
                    }
                }

                // Filtered Results
                item {
                    ProductCarousel(filtered, navController)
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Surface(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
        )
    }
}

@Composable
private fun ProductCarousel(products: List<Product>, navController: NavController) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        items(products) { product ->
            Box(modifier = Modifier.width(280.dp)) {
                ProductCardWithCarousel(product, navController)
            }
        }
    }
}
