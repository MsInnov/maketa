package com.mscode.data.products.datasource

import com.mscode.domain.products.model.Product

class ProductLocalDataSource {

    private var _products: List<Product> = emptyList()

    val products: List<Product>
        get() = _products

    fun saveProducts(products: List<Product>) {
        _products = products
    }
}