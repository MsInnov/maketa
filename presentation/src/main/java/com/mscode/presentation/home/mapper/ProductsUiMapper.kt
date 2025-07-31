package com.mscode.presentation.home.mapper

import com.mscode.domain.favorites.model.FavoriteProducts
import com.mscode.domain.products.model.Product
import com.mscode.presentation.home.model.UiProducts

class ProductsUiMapper {

    fun toProductsUi(
        products: Product,
        isCart: Boolean = false
    ) = UiProducts(
        id = products.id,
        title = products.title,
        price = products.price,
        description = products.description,
        category = products.category,
        image = products.image,
        isFavorite = products.isFavorite,
        isCart = isCart
    )

    fun toProductsUi(products: FavoriteProducts) = UiProducts(
        id = products.id,
        title = products.title,
        price = products.price,
        description = products.description,
        category = products.category,
        image = products.image,
        isCart = products.isCart,
        isFavorite = true
    )

    fun toFavoriteProducts(uiProducts: UiProducts) = FavoriteProducts(
        id = uiProducts.id,
        title = uiProducts.title,
        image = uiProducts.image,
        price = uiProducts.price,
        category = uiProducts.category,
        description = uiProducts.description,
        isCart = uiProducts.isCart
    )
}