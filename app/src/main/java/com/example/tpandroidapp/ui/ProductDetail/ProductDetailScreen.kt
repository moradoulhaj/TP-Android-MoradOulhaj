import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tpandroidapp.ui.ProductDetail.ProductDetailIntent
import com.example.tpandroidapp.ui.ProductDetail.ProductDetailState
import com.example.tpandroidapp.ui.ProductDetail.ProductDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavController,
    viewModel: ProductDetailViewModel = viewModel()
) {
    val state by viewModel.state

    LaunchedEffect(productId) {
        viewModel.handleIntent(ProductDetailIntent.LoadProduct(productId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Détails du produit") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (state) {
                is ProductDetailState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProductDetailState.Success -> {
                    val product = (state as ProductDetailState.Success).product
                    Column(modifier = Modifier.padding(16.dp)) {
                        AsyncImage(
                            model = product.img1,
                            contentDescription = product.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = product.name, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Prix : ${product.price} MAD", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = product.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                is ProductDetailState.Error -> {
                    Text(
                        text = "Erreur : ${(state as ProductDetailState.Error).message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
