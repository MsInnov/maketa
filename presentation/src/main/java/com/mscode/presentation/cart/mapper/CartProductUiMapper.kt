package com.mscode.presentation.cart.mapper

import com.mscode.domain.products.model.Product
import com.mscode.presentation.home.model.UiProduct

class CartProductUiMapper {

    fun toCart(
        uiCart: UiProduct.Cart
    ): Product.Cart = Product.Cart(
        id = uiCart.id,
        description = uiCart.description,
        category = uiCart.category,
        price = uiCart.price,
        image = uiCart.image,
        title = uiCart.title
    )

    fun toUiCart(
        cart: Product.Cart
    ): UiProduct.Cart = UiProduct.Cart(
        id = cart.id,
        description = cart.description,
        category = cart.category,
        price = cart.price,
        image = cart.image,
        title = cart.title
    )

}