package com.mscode.data.favorites.repository

import com.mscode.data.favorites.datasource.FavoriteLocalDataSource
import com.mscode.data.favorites.mapper.FavoriteMapper
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.favorites.model.FavoriteProducts
import com.mscode.domain.favorites.repository.FavoriteRepository
import com.mscode.domain.products.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception

class FavoriteRepositoryImpl(
    private val favoriteLocalDataSource: FavoriteLocalDataSource,
    private val favoriteMapper: FavoriteMapper
) : FavoriteRepository {

    override suspend fun addFavorites(favoriteProducts: FavoriteProducts) =
        favoriteLocalDataSource.insertFavorite(favoriteMapper.toProductsEntity(favoriteProducts))

    override suspend fun deleteFavorites(favoriteProducts: FavoriteProducts) =
        favoriteLocalDataSource.deleteFavorite(favoriteMapper.toProductsEntity(favoriteProducts))

    override suspend fun getFavorites(): List<FavoriteProducts> =
        favoriteLocalDataSource.getAllFavorites().map { favorite ->
            favoriteMapper.toFavoriteProducts(favorite)
        }

    override suspend fun getFavorite(id: Int): WrapperResults<FavoriteProducts> =
        favoriteLocalDataSource.getFavoriteById(id)?.let {
            WrapperResults.Success(favoriteMapper.toFavoriteProducts(it))
        } ?: WrapperResults.Error(Exception())

    override fun saveFavoriteIsDisplayed(isDisplayed: Boolean) {
        favoriteLocalDataSource.saveFavoriteIsDisplayed(isDisplayed)
    }

    override fun getIsDisplayed(): Boolean = favoriteLocalDataSource.isDisplayed

    override suspend fun getFavoritesFilteredByCategory(category: String): List<Product> =
        favoriteLocalDataSource.getAllFavorites()
            .filter { favorites -> favorites.category == category }
            .map { favoritesProduct -> favoriteMapper.toProducts(favoritesProduct) }

    override fun getFavoritesFilteredByCategoryFlow(category: String): Flow<List<Product>> =
        favoriteLocalDataSource.getAllFavoritesFlow()
            .map { products ->
                products
                    .filter { favorites -> favorites.category == category }
                    .map { favoritesProduct -> favoriteMapper.toProducts(favoritesProduct) }
            }

    override fun getFavoritesFlow(): Flow<List<FavoriteProducts>> =
        favoriteLocalDataSource.getAllFavoritesFlow()
            .map { favoritesProduct ->
                favoritesProduct.map { favorite ->
                    favoriteMapper.toFavoriteProducts(
                        favorite,
                    )
                }
            }
}