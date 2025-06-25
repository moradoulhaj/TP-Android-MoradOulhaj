package com.example.tpandroidapp.ui.Cart

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.tpandroidapp.R
import com.example.tpandroidapp.data.model.OrderRequest
import com.example.tpandroidapp.data.model.UserData

@Composable
fun CartScreen(
    userData: UserData,
    viewModel: CartViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state
    val context = LocalContext.current
    var address by remember { mutableStateOf("") }

    // User data
    val token = userData.token ?: ""
    val userId = userData.id?.toString() ?: ""
    val fullname = userData.fullname ?: ""
    val phone = userData.phone ?: ""

    // Localized strings (to avoid using them inside onClick)
    val enterValidAddressText = stringResource(id = R.string.enter_valid_address)
    val yourCartLabel = stringResource(id = R.string.your_cart)
    val emptyCartText = stringResource(id = R.string.empty_cart)
    val quantityLabel = stringResource(id = R.string.quantity_label)
    val orderSummaryText = stringResource(id = R.string.order_summary)
    val totalLabel = stringResource(id = R.string.total)
    val deliveryAddressLabel = stringResource(id = R.string.delivery_address)
    val placeOrderText = stringResource(id = R.string.place_order)
    val orderSuccessText = stringResource(id = R.string.order_success)

    // Load cart and handle events
    LaunchedEffect(Unit) {
        viewModel.handleIntent(CartIntent.LoadCart(token))
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            if (event is CartViewModel.UiEvent.ShowToast) {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    when (val cartState = state) {
        is CartState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is CartState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = cartState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        is CartState.OrderPlaced -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        orderSuccessText,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }

        is CartState.Success -> {
            val cartItems = cartState.items

            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emptyCartText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            } else {
                val totalAmount = cartItems.sumOf { it.priceProduct * it.count }

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text(
                        yourCartLabel,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    cartItems.forEach { cartItem ->
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            tonalElevation = 2.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                cartItem.img?.let {
                                    Image(
                                        painter = rememberAsyncImagePainter(it),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(cartItem.nameProduct, fontWeight = FontWeight.SemiBold)
                                    Text(
                                        "$quantityLabel: ${cartItem.count}",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        "${cartItem.priceProduct} MAD",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(orderSummaryText, fontWeight = FontWeight.Bold)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(totalLabel)
                                Text(
                                    "$totalAmount MAD",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Text(
                        deliveryAddressLabel,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    BasicTextField(
                        value = address,
                        onValueChange = { address = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                MaterialTheme.shapes.medium
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(12.dp),
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (address.isBlank()) {
                                Toast.makeText(context, enterValidAddressText, Toast.LENGTH_SHORT).show()
                            } else {
                                val currentCart = (state as? CartState.Success)?.items ?: emptyList()
                                val total = currentCart.sumOf { it.priceProduct * it.count }.toInt()

                                val orderRequest = OrderRequest(
                                    idUser = userData.id ?: 0,
                                    phone = phone,
                                    address = address,
                                    fullname = fullname,
                                    total = total,
                                    cart = (0..99).random() // test cart id
                                )

                                viewModel.handleIntent(CartIntent.PlaceOrder(orderRequest))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(placeOrderText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

