package com.example.tpandroidapp.ui.Order


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tpandroidapp.data.model.UserData

@Composable
fun UserOrdersScreen(
    userData: UserData,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val context = LocalContext.current
    val userId = userData.id ?: return

    LaunchedEffect(userId) {
        viewModel.handleIntent(OrderIntent.LoadUserOrders(userId))
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            if (event is OrderViewModel.UiEvent.ShowToast) {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    when (val currentState = state) {
        is OrderState.Loading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is OrderState.Success -> {
            if (currentState.orders.isEmpty()) {
                Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Aucune commande trouvée.",
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(currentState.orders) { order ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    text = "Commande #${order.id}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text("Nom : ${order.fullname}")
                                Text("Téléphone : ${order.phone}")
                                Text("Adresse : ${order.address}")
                                Text("Total : ${order.total} MAD")
                                Text("Date : ${order.createdAt}")
                                Text("Livrée : ${if (order.isDelivered) "Oui" else "Non"}")
                            }
                        }
                    }
                }
            }
        }

        is OrderState.Error -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Erreur : ${currentState.message}", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
