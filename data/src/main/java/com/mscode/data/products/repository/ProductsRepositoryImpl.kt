package com.mscode.data.products.repository

import com.mscode.data.cart.datasource.CartLocalDataSource
import com.mscode.data.favorites.datasource.FavoriteLocalDataSource
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductsRepositoryImpl(
    private val localConfigDataSource: LocalConfigDataSource,
    private val retrofitFactory: RetrofitFactory,
    private val productsMapper: ProductsMapper,
    private val favoritesLocalDataSource: FavoriteLocalDataSource,
    private val cartLocalDataSource: CartLocalDataSource,
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
                        productsEntity,
                        isFavorite(productsEntity.id)
                    )
                }
                localProductsDataSource.saveProducts(products)
                WrapperResults.Success(products)
            }

            is WrapperResults.Error -> result
        }
    }

    override suspend fun getProductsFilteredByCategory(category: String): List<Product> =
        localProductsDataSource.products
            .filter { it.category == category }
            .verifyAndAddFavorite()

    override fun getCategoryProducts(): List<String> =
        localProductsDataSource.products.map { it.category }.distinct()

    override suspend fun sellProduct(product: Product): WrapperResults<Unit> {
        val baseUrl = localConfigDataSource.urls.firstOrNull { it.key == url_products }
            ?: return WrapperResults.Error(Exception("Product URL missing"))
        val api = retrofitFactory.create(baseUrl.value, ProductsApi::class.java)
        val remoteDataSource = ProductRemoteDataSource(api)

        return remoteDataSource.newProduct(productsMapper.toProductEntity(product))
    }

    private suspend fun isFavorite(id: Int) = favoritesLocalDataSource.getFavoriteById(id) != null

    private suspend fun List<Product>.verifyAndAddFavorite(): List<Product> = map { products ->
        products.copy(isFavorite = isFavorite(products.id))
    }

    override suspend fun isCartProducts(): Flow<List<Pair<Int, Boolean>>> =
        cartLocalDataSource.getCartByFlow().map { carts ->
            localProductsDataSource.products.map { product ->
                val cart = carts.firstOrNull { it.id == product.id }
                if(cart == null) {
                    product.id to false
                } else {
                    product.id to true
                }
            }
        }

}