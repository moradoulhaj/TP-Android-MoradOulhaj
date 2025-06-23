package com.example.tpandroidapp.ui.ProductList

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
                Text("Erreur : ${(state as ProductListState.Error).message}")
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // 🔹 New Arrivals Section
                item {
                    Text("🆕 New Arrivals", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(newArrivals) { product ->
                            Box(modifier = Modifier.width(280.dp)) {
                                ProductCardWithCarousel(product, navController)
                            }
                        }
                    }
                }

                // 🔹 Best Deals Section
                item {
                    Text("🔥 Best Deals", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(bestDeals) { product ->
                            Box(modifier = Modifier.width(280.dp)) {
                                ProductCardWithCarousel(product, navController)
                            }
                        }
                    }
                }

                // 🔹 Category Filter
                item {
                    Text("📂 Filter by Category", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            FilterChip(
                                selected = selectedCategory == null,
                                onClick = { selectedCategory = null },
                                label = { Text("All") }
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

                // 🔹 Filtered Products Carousel
                item {
                    Text("🔍 Filtered Products", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(filtered) { product ->
                            Box(modifier = Modifier.width(280.dp)) {
                                ProductCardWithCarousel(product, navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
