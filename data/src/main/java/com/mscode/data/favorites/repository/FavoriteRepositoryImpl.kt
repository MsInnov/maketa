package com.mscode.data.favorites.repository

import com.mscode.data.cart.datasource.CartLocalDataSource
import com.mscode.data.favorites.datasource.FavoriteLocalDataSource
import com.mscode.data.favorites.mapper.FavoriteMapper
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.favorites.repository.FavoriteRepository
import com.mscode.domain.products.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception

class FavoriteRepositoryImpl(
    private val favoriteLocalDataSource: FavoriteLocalDataSource,
    private val cartLocalDataSource: CartLocalDataSource,
    private val favoriteMapper: FavoriteMapper
) : FavoriteRepository {

    override suspend fun addFavorite(favorite: Product.Favorite) =
        favoriteLocalDataSource.insertFavorite(favoriteMapper.toProductsEntity(favorite))

    override suspend fun deleteFavorite(favorite: Product.Favorite) =
        favoriteLocalDataSource.deleteFavorite(favoriteMapper.toProductsEntity(favorite))

    override suspend fun getFavorites(): List<Product.Favorite> =
        favoriteLocalDataSource.getAllFavorites().map { favorite ->
            favoriteMapper.toFavoriteProducts(favorite, isCart(favorite.id))
        }

    override suspend fun getFavorite(id: Int): WrapperResults<Product.Favorite> =
        favoriteLocalDataSource.getFavoriteById(id)?.let {
            WrapperResults.Success(favoriteMapper.toFavoriteProducts(it, isCart(it.id)))
        } ?: WrapperResults.Error(Exception())

    override fun saveFavoriteIsDisplayed(isDisplayed: Boolean) {
        favoriteLocalDataSource.saveFavoriteIsDisplayed(isDisplayed)
    }

    override fun getIsDisplayed(): Boolean = favoriteLocalDataSource.isDisplayed

    override suspend fun getFavoritesFilteredByCategory(category: String): List<Product.Classic> =
        favoriteLocalDataSource.getAllFavorites()
            .filter { favorites -> favorites.category == category }
            .map { favorite -> favoriteMapper.toProducts(favorite) }

    override fun getFavoritesFilteredByCategoryFlow(category: String): Flow<List<Product.Classic>> =
        favoriteLocalDataSource.getAllFavoritesFlow()
            .map { products ->
                products
                    .filter { favorites -> favorites.category == category }
                    .map { favoritesProduct -> favoriteMapper.toProducts(favoritesProduct) }
            }

    override fun getFavoritesFlow(): Flow<List<Product.Favorite>> =
        favoriteLocalDataSource.getAllFavoritesFlow()
            .map { favoritesProduct ->
                favoritesProduct.map { favorite ->
                    favoriteMapper.toFavoriteProducts(
                        favorite,
                        isCart(favorite.id)
                    )
                }
            }

    private suspend fun isCart(id: Int): Boolean =
        cartLocalDataSource.getCartProductById(id) != null
}