package com.mscode.presentation.cart.mapper

import com.mscode.domain.cart.model.CartProduct
import com.mscode.presentation.cart.model.UiCartProduct

class CartProductUiMapper {

    fun toCartProduct(
        uiCartProduct: UiCartProduct
    ): CartProduct = CartProduct(
        id = uiCartProduct.id,
        description = uiCartProduct.description,
        category = uiCartProduct.category,
        price = uiCartProduct.price,
        image = uiCartProduct.image,
        title = uiCartProduct.title
    )

    fun toUiCartProduct(
        uiCartProduct: CartProduct
    ): UiCartProduct = UiCartProduct(
        id = uiCartProduct.id,
        description = uiCartProduct.description,
        category = uiCartProduct.category,
        price = uiCartProduct.price,
        image = uiCartProduct.image,
        title = uiCartProduct.title
    )

}