package com.mscode.domain.favorites.repository

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.favorites.model.FavoriteProducts
import com.mscode.domain.products.model.Product
import kotlinx.coroutines.flow.Flow


interface FavoriteRepository {

    suspend fun addFavorites(favoriteProducts: FavoriteProducts): Long

    suspend fun deleteFavorites(favoriteProducts: FavoriteProducts): Int

    suspend fun getFavorites(): List<FavoriteProducts>

    suspend fun getFavoritesFilteredByCategory(category: String): List<Product>

    fun getFavoritesFilteredByCategoryFlow(category: String): Flow<List<Product>>

    suspend fun getFavorite(id: Int): WrapperResults<FavoriteProducts>

    fun saveFavoriteIsDisplayed(isDisplayed: Boolean)

    fun getIsDisplayed(): Boolean

    fun getFavoritesFlow(): Flow<List<FavoriteProducts>>

}