package com.example.tpandroidapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tpandroidapp.ui.ProductList.ProductListState
import com.example.tpandroidapp.ui.ProductList.ProductListViewModel
import com.example.tpandroidapp.ui.ProductList.ProductListIntent
import com.example.tpandroidapp.ui.ProductItem


@Composable
fun ProductListScreen(
    navController: NavController,
    viewModel: ProductListViewModel = viewModel()
) {
    val state by viewModel.state

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProductListIntent.LoadProducts)
    }

    when (state) {
        is ProductListState.Loading -> {
            Text("Chargement des produits...")
        }

        is ProductListState.Success -> {
            val products = (state as ProductListState.Success).products
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    ProductItem(product = product, navController = navController)
                }
            }
        }

        is ProductListState.Error -> {
            Text("Erreur : ${(state as ProductListState.Error).message}")
        }
    }
}
