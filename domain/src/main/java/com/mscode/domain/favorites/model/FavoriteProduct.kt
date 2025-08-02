package com.mscode.domain.favorites.model

data class FavoriteProduct (
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val isCart: Boolean
)