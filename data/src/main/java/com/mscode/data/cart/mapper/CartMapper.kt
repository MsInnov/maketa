package com.mscode.data.cart.mapper

import com.mscode.data.cart.model.CartProductEntity
import com.mscode.domain.cart.model.CartProduct

class CartMapper {

    fun toCartLocalEntity(cartProduct: CartProduct): CartProductEntity =
        CartProductEntity(
            id = cartProduct.id,
            title = cartProduct.title,
            price = cartProduct.price,
            image = cartProduct.image,
            category = cartProduct.category,
            description = cartProduct.description
        )

    fun toCartProducts(
        cart: List<CartProductEntity>
    ): List<CartProduct> =
        cart.map { cartProductEntity ->
            CartProduct(
                id = cartProductEntity.id,
                title = cartProductEntity.title,
                image = cartProductEntity.image,
                price = cartProductEntity.price,
                category = cartProductEntity.category,
                description = cartProductEntity.description
            )
        }

}