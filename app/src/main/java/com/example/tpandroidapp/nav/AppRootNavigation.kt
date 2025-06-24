package com.example.tpandroidapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.tpandroidapp.ui.Login.LoginScreen
import com.example.tpandroidapp.ui.MainScreen
import com.example.tpandroidapp.ui.ProductDetail.ProductDetailScreen
import com.example.tpandroidapp.ui.ProductList.ProductListScreen
import com.example.tpandroidapp.ui.signup.SignupScreen

@Composable
fun AppRootNavigation(navController: NavHostController, isLoggedIn: Boolean) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "main" else "auth"
    ) {
        navigation(startDestination = "login", route = "auth") {
            composable("login") { LoginScreen(navController) }
            composable("signup") { SignupScreen(navController) }
        }

        navigation(startDestination = "home", route = "main") {
            composable("home") { MainScreen(navController) }
            composable("product_detail/{productId}") { backStackEntry ->
                val productIdString = backStackEntry.arguments?.getString("productId") ?: "0"
                val productId = productIdString.toIntOrNull() ?: 0 // safe conversion, default 0 if invalid

                ProductDetailScreen(productId, navController)
            }
            // Add other main routes here (cart, commandes, etc.)
        }
    }
}
