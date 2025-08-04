package com.mscode.domain.products.repository

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.products.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    suspend fun getProducts(): WrapperResults<List<Product.Classic>>
    suspend fun getProductsFilteredByCategory(category: String): List<Product.Classic>
    fun getCategoryProducts(): List<String>
    suspend fun sellProduct(product: Product.Classic): WrapperResults<Unit>
    suspend fun isCartProducts(): Flow<List<Pair<Int, Boolean>>>

}