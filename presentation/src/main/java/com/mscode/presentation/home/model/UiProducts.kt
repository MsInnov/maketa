package com.mscode.presentation.home.model

data class UiProducts (
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val isFavorite: Boolean,
    val isCart: Boolean
)