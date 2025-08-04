package com.mscode.data.cart.mapper

import com.mscode.data.cart.model.CartEntity
import com.mscode.domain.products.model.Product

class CartMapper {

    fun toCartEntity(cart: Product.Cart): CartEntity =
        CartEntity(
            id = cart.id,
            title = cart.title,
            price = cart.price,
            image = cart.image,
            category = cart.category,
            description = cart.description
        )

    fun toCartProducts(
        cartEntities: List<CartEntity>
    ): List<Product.Cart> =
        cartEntities.map { cartEntity ->
            Product.Cart(
                id = cartEntity.id,
                title = cartEntity.title,
                image = cartEntity.image,
                price = cartEntity.price,
                category = cartEntity.category,
                description = cartEntity.description
            )
        }

}