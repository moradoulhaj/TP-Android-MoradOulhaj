package com.example.tpandroidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.tpandroidapp.ui.auth.AuthViewModel
import com.example.tpandroidapp.ui.navigation.AppNavigation
import com.example.tpandroidapp.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()

                // Observe l'état de connexion
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

                AppNavigation(
                    navController = navController,
                    isLoggedIn = isLoggedIn
                )
            }
        }
    }
}