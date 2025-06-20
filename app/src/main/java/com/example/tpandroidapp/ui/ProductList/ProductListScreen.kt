package com.example.tpandroidapp.ui.ProductList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tpandroidapp.data.model.Product
import coil.compose.AsyncImage
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment





@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = viewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    val products = state.products
        .filter { state.selectedCategory == null || it.category == state.selectedCategory }

    LaunchedEffect(Unit) {
        viewModel.onIntent(ProductListIntent.LoadProducts)
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

        Text(
            "Book Store",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF6A1B9A),
            modifier = Modifier.padding(16.dp)
        )

        CategoryFilterRow(
            categories = listOf("All") + state.products.map { it.category }.distinct(),
            selected = state.selectedCategory,
            onSelect = { cat ->
                viewModel.onIntent(ProductListIntent.FilterByCategory(if (cat == "All") null else cat))
            }
        )

        LazyColumn(
            modifier = Modifier.padding(8.dp)
        ) {
            item {
                Text("New Arrivals", style = MaterialTheme.typography.titleLarge, color = Color(0xFF6A1B9A), modifier = Modifier.padding(8.dp))
            }
            items(products.take(4)) { product ->
                ProductCard(product = product, navController)
            }

            item {
                Text("Best Deals", style = MaterialTheme.typography.titleLarge, color = Color(0xFF6A1B9A), modifier = Modifier.padding(8.dp))
            }
            items(products.sortedByDescending { it.promotionPercent }) { product ->
                ProductCard(product = product, navController)
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        state.errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun CategoryFilterRow(categories: List<String>, selected: String?, onSelect: (String) -> Unit) {
    LazyRow(modifier = Modifier.padding(horizontal = 8.dp)) {
        items(categories) { category ->
            val isSelected = category == selected || (category == "All" && selected == null)
            Button(
                onClick = { onSelect(category) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) Color(0xFF6A1B9A) else Color.LightGray,
                    contentColor = if (isSelected) Color.White else Color.Black
                ),
                modifier = Modifier
                    .padding(4.dp)
                    .height(36.dp)
            ) {
                Text(category)
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, navController: NavController) {
    Card(
        onClick = { navController.navigate("product_detail/${product.id}") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = product.img1,
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(4.dp)
            )

            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium, color = Color(0xFF6A1B9A))
                Text("${product.price / 100.0} USD", style = MaterialTheme.typography.bodyMedium)
                if (product.promotionPercent > 0) {
                    Text("-${product.promotionPercent}%", color = Color.Red)
                }
            }
        }
    }
}


