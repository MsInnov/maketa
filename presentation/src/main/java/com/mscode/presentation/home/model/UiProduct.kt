package com.mscode.presentation.home.model

sealed class UiProduct(
    open val id: Int,
    open val title: String,
    open val price: Double,
    open val description: String,
    open val category: String,
    open val image: String,
    open val isFavorite: Boolean,
    open val isCart: Boolean
) {
    data class Classic (
        override val id: Int,
        override val title: String,
        override val price: Double,
        override val description: String,
        override val category: String,
        override val image: String,
        override val isFavorite: Boolean,
        override val isCart: Boolean
    ): UiProduct(id = id, title = title, price = price, description = description, category = category, image = image, isFavorite = isFavorite, isCart = isCart)

    data class Sell(
        override val title: String,
        override val price: Double,
        override val description: String,
        override val category: String,
        override val image: String
    ): UiProduct(id = -1, title = title, price = price, description = description, category = category, image = image, isFavorite = false, isCart = false)

    data class Cart(
        override val id: Int,
        override val title: String,
        override val price: Double,
        override val description: String,
        override val category: String,
        override val image: String
    ): UiProduct(id = id, title = title, price = price, description = description, category = category, image = image, isFavorite = false, isCart = false)

}

