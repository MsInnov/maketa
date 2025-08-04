package com.mscode.presentation.home.mapper

import com.mscode.domain.products.model.Product
import com.mscode.presentation.home.model.UiProduct

class ProductsUiMapper {

    fun toProductUi(
        product: Product.Classic,
        isCart: Boolean = false
    ) = UiProduct.Classic(
        id = product.id,
        title = product.title,
        price = product.price,
        description = product.description,
        category = product.category,
        image = product.image,
        isFavorite = product.isFavorite,
        isCart = isCart
    )

    fun toProductUi(favorite: Product.Favorite) = UiProduct.Classic(
        id = favorite.id,
        title = favorite.title,
        price = favorite.price,
        description = favorite.description,
        category = favorite.category,
        image = favorite.image,
        isCart = favorite.isCart,
        isFavorite = true
    )

    fun toFavorite(uiProduct: UiProduct.Classic) = Product.Favorite(
        id = uiProduct.id,
        title = uiProduct.title,
        image = uiProduct.image,
        price = uiProduct.price,
        category = uiProduct.category,
        description = uiProduct.description,
        isCart = uiProduct.isCart
    )

    fun toCart(uiProduct: UiProduct.Classic) = Product.Cart(
        id = uiProduct.id,
        title = uiProduct.title,
        image = uiProduct.image,
        price = uiProduct.price,
        category = uiProduct.category,
        description = uiProduct.description
    )

    fun toProducts(list: List<UiProduct.Classic>) = list.map { product ->
        Product.Classic(
            id = product.id,
            title = product.title,
            image = product.image,
            price = product.price,
            category = product.category,
            description = product.description,
            isFavorite = product.isFavorite
        )
    }
}