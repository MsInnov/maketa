package com.mscode.presentation.home.mapper

import com.mscode.domain.cart.model.CartProduct
import com.mscode.domain.favorites.model.FavoriteProduct
import com.mscode.domain.products.model.Product
import com.mscode.presentation.home.model.UiProduct

class ProductsUiMapper {

    fun toProductUi(
        product: Product,
        isCart: Boolean = false
    ) = UiProduct(
        id = product.id,
        title = product.title,
        price = product.price,
        description = product.description,
        category = product.category,
        image = product.image,
        isFavorite = product.isFavorite,
        isCart = isCart
    )

    fun toProductUi(product: FavoriteProduct) = UiProduct(
        id = product.id,
        title = product.title,
        price = product.price,
        description = product.description,
        category = product.category,
        image = product.image,
        isCart = product.isCart,
        isFavorite = true
    )

    fun toFavoriteProduct(uiProduct: UiProduct) = FavoriteProduct(
        id = uiProduct.id,
        title = uiProduct.title,
        image = uiProduct.image,
        price = uiProduct.price,
        category = uiProduct.category,
        description = uiProduct.description,
        isCart = uiProduct.isCart
    )

    fun toCartProduct(uiProduct: UiProduct) = CartProduct(
        id = uiProduct.id,
        title = uiProduct.title,
        image = uiProduct.image,
        price = uiProduct.price,
        category = uiProduct.category,
        description = uiProduct.description
    )
}