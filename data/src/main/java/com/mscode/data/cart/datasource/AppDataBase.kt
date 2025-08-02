package com.mscode.data.cart.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mscode.data.cart.model.CartProductEntity

@Database(entities = [CartProductEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}