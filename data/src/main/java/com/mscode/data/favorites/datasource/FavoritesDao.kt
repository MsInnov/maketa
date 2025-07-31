package com.mscode.data.favorites.datasource

import androidx.room.*
import com.mscode.data.favorites.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteEntity: FavoriteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favoriteEntities: List<FavoriteEntity>)

    @Delete
    suspend fun delete(favoriteEntity: FavoriteEntity): Int

    @Query("SELECT * FROM favorite")
    suspend fun getAll(): List<FavoriteEntity>

    @Query("SELECT * FROM favorite WHERE id = :id")
    suspend fun getById(id: Int): FavoriteEntity?

    @Query("SELECT * FROM favorite")
    fun getAllFlow(): Flow<List<FavoriteEntity>>
}