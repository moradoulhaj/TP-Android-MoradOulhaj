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
import com.example.tpandroidapp.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

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

    // Localized strings
    val noOrdersText = stringResource(id = R.string.no_orders_found)
    val orderLabel = stringResource(id = R.string.order_number)
    val nameLabel = stringResource(id = R.string.name)
    val phoneLabel = stringResource(id = R.string.phone)
    val addressLabel = stringResource(id = R.string.address)
    val totalLabel = stringResource(id = R.string.total)
    val dateLabel = stringResource(id = R.string.date)
    val deliveredLabel = stringResource(id = R.string.delivered)
    val yes = stringResource(id = R.string.yes)
    val no = stringResource(id = R.string.no)
    val errorLabel = stringResource(id = R.string.error)

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
            Box(
                modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        is OrderState.Success -> {
            if (currentState.orders.isEmpty()) {
                Box(
                    modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = noOrdersText,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(currentState.orders) { order ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                Modifier
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "$orderLabel #${order.id}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Text("$nameLabel : ${order.fullname}", style = MaterialTheme.typography.bodyMedium)
                                Text("$phoneLabel : ${order.phone}", style = MaterialTheme.typography.bodyMedium)
                                Text("$addressLabel : ${order.address}", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "$totalLabel : ${order.total} MAD",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    "$dateLabel : ${order.createdAt}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "$deliveredLabel : ${if (order.delivery) yes else no}",
                                    fontWeight = FontWeight.Medium,
                                    color = if (order.delivery) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

        is OrderState.Error -> {
            Box(
                modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "$errorLabel : ${currentState.message}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


