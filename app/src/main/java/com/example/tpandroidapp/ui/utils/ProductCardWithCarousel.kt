package com.example.tpandroidapp.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tpandroidapp.data.model.Product

@Composable
fun ProductCardWithCarousel(product: Product, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("product_detail/${product.id}") }
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val imageUrls = listOfNotNull(product.img1, product.img2, product.img3, product.img4)

            if (imageUrls.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(imageUrls.withIndex().toList(), key = { "${it.index}_${it.value}" }) { indexedUrl ->
                        val url = indexedUrl.value
                        Image(
                            painter = rememberAsyncImagePainter(url),
                            contentDescription = null,
                            modifier = Modifier
                                .width(140.dp)
                                .height(160.dp)
                        )
                    }

                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )

            product.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text(
                    text = "${product.price} MAD",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                product.originalPrice?.takeIf { it > product.price }?.let { original ->
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$original MAD",
                        style = MaterialTheme.typography.bodySmall.copy(
                            textDecoration = TextDecoration.LineThrough
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                product.promotionPercent?.takeIf { it > 0 }?.let { percent ->
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "-$percent%",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
