package com.example.tpandroidapp.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import androidx.navigation.NavHostController
import com.example.tpandroidapp.data.datastore.UserPreferences
import com.example.tpandroidapp.ui.Cart.CartScreen
import com.example.tpandroidapp.ui.ProductList.ProductListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf("home") }

    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val userData by userPreferences.userFlow.collectAsState(initial = null)
    val userToken = userData?.token ?: ""

    val avatarUrl = userData?.fullname?.replace(" ", "+")?.let {
        "https://ui-avatars.com/api/?name=$it"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "BOOKHAVEN",
                        color = Color.White // text color white
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF6200EE) // purple background
                ),
                actions = {
                    if (avatarUrl != null) {
                        AsyncImage(
                            model = avatarUrl,
                            contentDescription = "User Avatar",
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(36.dp)
                                .clip(CircleShape) // rounded avatar
                        )
                    } else {
                        IconButton(onClick = { /* TODO: profile or menu */ }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "User Avatar",
                                tint = Color.White // icon white tint
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedTab == "home",
                    onClick = { selectedTab = "home" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
                    label = { Text("Cart") },
                    selected = selectedTab == "cart",
                    onClick = { selectedTab = "cart" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Commandes") },
                    label = { Text("Commandes") },
                    selected = selectedTab == "commandes",
                    onClick = { selectedTab = "commandes" }
                )
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            "home" -> ProductListScreen(
                navController = navController,
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            )
            "cart" -> CartScreen(
                userToken = userToken,
                viewModel = hiltViewModel(),
                modifier = Modifier.fillMaxSize()
            )
            // "commandes" -> OrdersScreen(navController, Modifier.padding(paddingValues))
        }
    }
}
