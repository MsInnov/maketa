package com.mscode.domain.favorites.repository

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.products.model.Product
import kotlinx.coroutines.flow.Flow


interface FavoriteRepository {

    suspend fun addFavorite(favorite: Product.Favorite): Long

    suspend fun deleteFavorite(favorite: Product.Favorite): Int

    suspend fun getFavorites(): List<Product.Favorite>

    suspend fun getFavoritesFilteredByCategory(category: String): List<Product.Classic>

    fun getFavoritesFilteredByCategoryFlow(category: String): Flow<List<Product.Classic>>

    suspend fun getFavorite(id: Int): WrapperResults<Product.Favorite>

    fun saveFavoriteIsDisplayed(isDisplayed: Boolean)

    fun getIsDisplayed(): Boolean

    fun getFavoritesFlow(): Flow<List<Product.Favorite>>

}