package com.example.tpandroidapp.data

import com.example.tpandroidapp.R
import com.example.tpandroidapp.model.Product

object ProductRepository {

    val productList = listOf(
        Product("1", "Phone", 699.99, R.drawable.iphone),
        Product("2", "Laptop", 1299.99, R.drawable.laptop),
        Product("3", "Headphones", 199.99, R.drawable.headphones)
    )

    fun getProductById(id: String): Product? {
        return productList.find { it.id == id }
    }

    fun getAllProducts(): List<Product> {
        return productList
    }
}
