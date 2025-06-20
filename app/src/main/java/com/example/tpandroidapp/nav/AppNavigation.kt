package com.example.tpandroidapp.ui.navigation

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tpandroidapp.ui.screens.ProductListScreen
import com.example.tpandroidapp.ui.screens.ProductDetailScreen
import com.example.tpandroidapp.ui.signup.SignupScreen

@Composable
fun AppNavigation(navController: NavHostController, isLoggedIn: Boolean) {
    // remember latest state to avoid recomposition issues
    val loggedInState by rememberUpdatedState(isLoggedIn)

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "product_list" else "login"
    ) {

        composable("login") {
            if (loggedInState) {
                LaunchedEffect(Unit) {
                    navController.navigate("product_list") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            } else {
                LoginScreen(navController = navController)
            }
        }

        composable("signup") {
            if (loggedInState) {
                LaunchedEffect(Unit) {
                    navController.navigate("product_list") {
                        popUpTo("signup") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            } else {
                SignupScreen(navController = navController)
            }
        }

        composable("product_list") {
            ProductListScreen(navController = navController)
        }

        composable("product_detail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = productId,
                navController = navController
            )
        }
    }
}
