package com.mscode.data.favorites.datasource

import com.mscode.data.favorites.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

class FavoriteLocalDataSource(private val dao: ProductDao) {

    private var _isDisplayed: Boolean = false

    val isDisplayed: Boolean
        get() = _isDisplayed

    suspend fun getAllFavorites(): List<FavoriteEntity> = dao.getAll()

    suspend fun getFavoriteById(id: Int): FavoriteEntity? = dao.getById(id)

    suspend fun insertFavorite(favoriteEntity: FavoriteEntity) = dao.insert(favoriteEntity)

    suspend fun deleteFavorite(favoriteEntity: FavoriteEntity) = dao.delete(favoriteEntity)

    fun getAllFavoritesFlow(): Flow<List<FavoriteEntity>> = dao.getAllFlow()

    fun saveFavoriteIsDisplayed(isDisplayed: Boolean) {
        _isDisplayed = isDisplayed
    }
}