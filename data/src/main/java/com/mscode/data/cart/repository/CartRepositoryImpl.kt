package com.mscode.data.cart.repository

import com.mscode.data.cart.datasource.CartLocalDataSource
import com.mscode.data.cart.mapper.CartMapper
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.cart.model.CartProduct
import com.mscode.domain.cart.repository.CartRepository

class CartRepositoryImpl(
    private val cartLocalDataSource: CartLocalDataSource,
    private val mapper: CartMapper
) : CartRepository {
    override suspend fun addCartProduct(product: CartProduct): Long =
        cartLocalDataSource.insertCartProduct(mapper.toCartLocalEntity(product))

    override suspend fun removeCartProduct(product: CartProduct): Int =
        cartLocalDataSource.deleteCartProduct(mapper.toCartLocalEntity(product))

    override suspend fun getCart(): WrapperResults<List<CartProduct>> {
        return WrapperResults.Success(
            mapper.toCartProducts(cartLocalDataSource.getCart())
        )
    }

    override suspend fun removeCart() {
        cartLocalDataSource.deleteCart()
    }
}