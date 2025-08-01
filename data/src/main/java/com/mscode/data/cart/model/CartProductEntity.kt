package com.mscode.data.cart.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cartProduct")
data class CartProductEntity (
    @PrimaryKey val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String
)