package com.mscode.data.favorites.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mscode.data.favorites.model.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
