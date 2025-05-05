package com.example.tpandroidapp.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tpandroidapp.ui.screens.ProductListScreen
import com.example.tpandroidapp.ui.screens.ProductDetailScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    // The NavHost manages the navigation
    NavHost(navController = navController, startDestination = "product_list") {
        // Define the route for the ProductListScreen
        composable("product_list") {
            ProductListScreen(navController = navController)
        }

        // Define the route for the ProductDetailScreen
        composable("product_detail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(productId = productId, navController = navController)
        }
    }
}
