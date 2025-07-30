package com.mscode.data.products.datasource

import com.mscode.data.products.api.ProductsApi
import com.mscode.data.products.model.ProductEntity
import com.mscode.domain.common.WrapperResults
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(
    private val api: ProductsApi
) {

    suspend fun getProducts(): WrapperResults<List<ProductEntity>> = try {
        WrapperResults.Success(api.getProducts())
    } catch (e: Exception) {
        WrapperResults.Error(e)
    }
}