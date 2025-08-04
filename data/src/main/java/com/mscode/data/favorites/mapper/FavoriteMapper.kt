package com.mscode.data.favorites.mapper

import com.mscode.data.favorites.model.FavoriteEntity
import com.mscode.domain.products.model.Product

class FavoriteMapper {

    fun toFavoriteProducts(
        productEntity: FavoriteEntity,
        isCart: Boolean
    ): Product.Favorite = Product.Favorite(
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
    ): Product.Classic = Product.Classic(
        id = productEntity.id,
        title = productEntity.title,
        price = productEntity.price,
        description = productEntity.description,
        category = productEntity.category,
        image = productEntity.image,
        isFavorite = true
    )

    fun toProductsEntity(favorite: Product.Favorite): FavoriteEntity = FavoriteEntity(
        id = favorite.id,
        title = favorite.title,
        price = favorite.price,
        description = favorite.description,
        category = favorite.category,
        image = favorite.image
    )
}