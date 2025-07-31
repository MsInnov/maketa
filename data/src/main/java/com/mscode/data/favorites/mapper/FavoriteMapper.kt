package com.mscode.data.favorites.mapper

import com.mscode.data.favorites.model.FavoriteEntity
import com.mscode.domain.favorites.model.FavoriteProducts
import com.mscode.domain.products.model.Product

class FavoriteMapper {

    fun toFavoriteProducts(
        productEntity: FavoriteEntity,
        isCart: Boolean = false
    ): FavoriteProducts = FavoriteProducts(
        id = productEntity.id,
        title = productEntity.title,
        price = productEntity.price,
        description = productEntity.description,
        category = productEntity.category,
        image = productEntity.image,
        isCart = isCart
    )

    fun toProducts(
        productEntity: FavoriteEntity
    ): Product = Product(
        id = productEntity.id,
        title = productEntity.title,
        price = productEntity.price,
        description = productEntity.description,
        category = productEntity.category,
        image = productEntity.image,
        isFavorite = true
    )

    fun toProductsEntity(favoriteProducts: FavoriteProducts): FavoriteEntity = FavoriteEntity(
        id = favoriteProducts.id,
        title = favoriteProducts.title,
        price = favoriteProducts.price,
        description = favoriteProducts.description,
        category = favoriteProducts.category,
        image = favoriteProducts.image
    )
}