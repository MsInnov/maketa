package com.mscode.data.products.datasource

import com.mscode.domain.products.model.Product

class ProductLocalDataSource {

    private var _products: List<Product.Classic> = emptyList()

    val products: List<Product.Classic>
        get() = _products

    fun saveProducts(products: List<Product.Classic>) {
        _products = products
    }
}