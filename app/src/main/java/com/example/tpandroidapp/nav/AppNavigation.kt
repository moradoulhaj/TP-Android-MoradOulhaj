package com.example.tpandroidapp.ui.navigation

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tpandroidapp.ui.screens.ProductListScreen
import com.example.tpandroidapp.ui.screens.ProductDetailScreen
import com.example.tpandroidapp.ui.signup.SignupScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {  // Démarre sur login

        // Login screen route
        composable("login") {
            LoginScreen(navController = navController)
        }

         //Signup screen route
        composable("signup") {
            SignupScreen(navController = navController)
        }

        // List screen route
        composable("product_list") {
            ProductListScreen(navController = navController)
        }

        // Detail screen route with argument
        composable("product_detail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = productId,
                navController = navController
            )
        }
    }
}
