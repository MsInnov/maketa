package com.mscode.data.cart.repository

import com.mscode.data.cart.datasource.CartLocalDataSource
import com.mscode.data.cart.mapper.CartMapper
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.cart.repository.CartRepository
import com.mscode.domain.products.model.Product

class CartRepositoryImpl(
    private val cartLocalDataSource: CartLocalDataSource,
    private val mapper: CartMapper
) : CartRepository {
    override suspend fun addCartProduct(cart: Product.Cart): Long =
        cartLocalDataSource.insertCartProduct(mapper.toCartEntity(cart))

    override suspend fun removeCartProduct(cart: Product.Cart): Int =
        cartLocalDataSource.deleteCartProduct(mapper.toCartEntity(cart))

    override suspend fun getCart(): WrapperResults<List<Product.Cart>> {
        return WrapperResults.Success(
            mapper.toCartProducts(cartLocalDataSource.getCart())
        )
    }

    override suspend fun removeCart() {
        cartLocalDataSource.deleteCart()
    }
}