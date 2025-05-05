package com.example.tpandroidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.tpandroidapp.ui.navigation.AppNavigation
import com.example.tpandroidapp.ui.theme.TPAndroidAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Wrapping your navigation setup in a Theme
            TPAndroidAppTheme {
                // Create a NavController
                val navController = rememberNavController()

                // Passing the NavController to the AppNavigation composable
                AppNavigation(navController = navController)
            }
        }
    }
}
