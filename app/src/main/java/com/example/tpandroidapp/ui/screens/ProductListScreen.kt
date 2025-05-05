package com.example.tpandroidapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.tpandroidapp.data.ProductRepository
import com.example.tpandroidapp.model.Product

@Composable
fun ProductListScreen(navController: NavController) {
    // Fetch the list of products from ProductRepository
    val productList = ProductRepository.productList

    // Display products in a LazyColumn
    LazyColumn {
        items(productList) { product ->  // 'items' to loop through the list
            ProductItem(product = product, navController = navController)
        }
    }
}

@Composable
fun ProductItem(product: Product, navController: NavController) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                Toast.makeText(context, "Clicked: ${product.name}", Toast.LENGTH_SHORT).show()
                navController.navigate("product_detail/${product.id}")
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFECECEC)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = product.imageResId),
                contentDescription = product.name,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .height(150.dp)
                    .width(150.dp) // or use `.size(150.dp)` for square
            )
            Text(
                text = product.name,
                color = Color.Black
            )
            Text(
                text = "$${product.price}",
                color = Color.Gray
            )
        }
    }
}
