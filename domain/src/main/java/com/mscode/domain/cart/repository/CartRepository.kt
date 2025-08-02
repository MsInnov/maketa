package com.mscode.domain.cart.repository

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.cart.model.CartProduct

interface CartRepository {

    suspend fun addCartProduct(product: CartProduct): Long
    suspend fun removeCartProduct(product: CartProduct): Int
    suspend fun removeCart()
    suspend fun getCart(): WrapperResults<List<CartProduct>>

}