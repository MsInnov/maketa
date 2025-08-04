package com.mscode.data.cart.datasource

import com.mscode.data.cart.model.CartEntity
import kotlinx.coroutines.flow.Flow

class CartLocalDataSource(private val dao: CartDao) {
    suspend fun getCart(): List<CartEntity> = dao.getCart()

    fun getCartByFlow(): Flow<List<CartEntity>> = dao.getCartByFlow()

    suspend fun insertCartProduct(cartEntity: CartEntity) = dao.insertCartProduct(cartEntity)

    suspend fun deleteCartProduct(cartEntity: CartEntity) = dao.deleteCartProduct(cartEntity)

    suspend fun deleteCart() = dao.deleteCart()

    suspend fun getCartProductById(id: Int) = dao.getCartProductById(id)
}