package com.example.tpandroidapp.data

import com.example.tpandroidapp.model.Product

object ProductRepository {

    // Sample product list to mimic fetching from a database or API
    val productList = listOf(
        Product("1", "Phone", 699.99, android.R.drawable.ic_menu_camera),
        Product("2", "Laptop", 1299.99, android.R.drawable.ic_menu_camera),
        Product("3", "Headphones", 199.99, android.R.drawable.ic_menu_camera)
    )

    // Function to get product by ID
    fun getProductById(id: String): Product? {
        return productList.find { it.id == id }
    }

    // Function to get all products
    fun getAllProducts(): List<Product> {
        return productList
    }
}
