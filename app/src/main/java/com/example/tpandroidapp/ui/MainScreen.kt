package com.example.tpandroidapp.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.tpandroidapp.ui.ProductList.ProductListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf("home") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BOOKHAVEN") },
                actions = {
                    // Example avatar placeholder - replace with your image or icon
                    IconButton(onClick = { /* TODO: profile or menu */ }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "User Avatar"
                        )
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
        // Content area for different tabs
        when (selectedTab) {
            "home" -> ProductListScreen(navController = navController, modifier = Modifier.fillMaxSize())

//            "cart" -> CartScreen(navController, Modifier.padding(paddingValues))
//            "commandes" -> OrdersScreen(navController, Modifier.padding(paddingValues))
        }
    }
}
