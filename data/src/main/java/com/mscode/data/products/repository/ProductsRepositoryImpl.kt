package com.mscode.data.products.repository

import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.products.api.ProductsApi
import com.mscode.data.products.datasource.ProductLocalDataSource
import com.mscode.data.products.datasource.ProductRemoteDataSource
import com.mscode.data.products.mapper.ProductsMapper
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.model.url_products
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.products.model.Product
import com.mscode.domain.products.repository.ProductsRepository

class ProductsRepositoryImpl(
    private val localConfigDataSource: LocalConfigDataSource,
    private val retrofitFactory: RetrofitFactory,
    private val productsMapper: ProductsMapper,
    private val localProductsDataSource: ProductLocalDataSource
) : ProductsRepository {

    override suspend fun getProducts(): WrapperResults<List<Product>> {
        val baseUrl = localConfigDataSource.urls.firstOrNull { it.key == url_products }
            ?: return WrapperResults.Error(Exception("Product URL missing"))
        val api = retrofitFactory.create(baseUrl.value, ProductsApi::class.java)
        val remoteDataSource = ProductRemoteDataSource(api)

        return when (val result = remoteDataSource.getProducts()) {
            is WrapperResults.Success -> {
                val products = result.data.map { productsEntity ->
                    productsMapper.toProducts(
                        productsEntity
                    )
                }
                localProductsDataSource.saveProducts(products)
                WrapperResults.Success(products)
            }

            is WrapperResults.Error -> result
        }
    }
}