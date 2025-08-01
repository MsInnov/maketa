package com.mscode.data.cart.datasource

import com.mscode.data.cart.model.CartProductEntity
import kotlinx.coroutines.flow.Flow

class CartLocalDataSource(private val dao: CartDao) {
    suspend fun getCart(): List<CartProductEntity> = dao.getCart()

    fun getCartByFlow(): Flow<List<CartProductEntity>> = dao.getCartByFlow()

    suspend fun insertCartProduct(cartProduct: CartProductEntity) = dao.insertCartProduct(cartProduct)

    suspend fun deleteCartProduct(cartProduct: CartProductEntity) = dao.deleteCartProduct(cartProduct)

    suspend fun deleteCart() = dao.deleteCart()

    suspend fun getCartProductById(id: Int) = dao.getCartProductById(id)
}