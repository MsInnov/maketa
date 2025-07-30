package com.mscode.domain.products.repository

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.products.model.Product

interface ProductsRepository {

    suspend fun getProducts(): WrapperResults<List<Product>>

}