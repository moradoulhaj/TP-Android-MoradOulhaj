package com.example.tpandroidapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tpandroidapp.data.ProductRepository

@Composable
fun ProductDetailScreen(productId: String, navController: NavController) {
    val product = ProductRepository.getProductById(productId)

    product?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = product.imageResId),
                contentDescription = product.name
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = product.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "$${product.price}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Back")
            }
        }
    } ?: run {
        Text("Product not found", modifier = Modifier.padding(16.dp))
    }
}
