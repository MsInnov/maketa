package com.mscode.domain.products.model

sealed class Product(
    open val id: Int,
    open val title: String,
    open val price: Double,
    open val description: String,
    open val category: String,
    open val image: String,
    open val isFavorite: Boolean
) {
    data class Classic(
        override val id: Int,
        override val title: String,
        override val price: Double,
        override val description: String,
        override val category: String,
        override val image: String,
        override val isFavorite: Boolean,
    ): Product(id, title, price, description, category, image, isFavorite)

    data class Cart(
        override val id: Int,
        override val title: String,
        override val price: Double,
        override val description: String,
        override val category: String,
        override val image: String,
    ): Product(id, title, price, description, category, image, false)

    data class Favorite(
        override val id: Int,
        override val title: String,
        override val price: Double,
        override val description: String,
        override val category: String,
        override val image: String,
        val isCart: Boolean
    ): Product(id, title, price, description, category, image, false)
}
