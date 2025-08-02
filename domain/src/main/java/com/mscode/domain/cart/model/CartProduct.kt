package com.mscode.domain.cart.model

data class CartProduct (
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String
)