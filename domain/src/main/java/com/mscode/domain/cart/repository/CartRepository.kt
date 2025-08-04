package com.mscode.domain.cart.repository

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.products.model.Product

interface CartRepository {

    suspend fun addCartProduct(product: Product.Cart): Long
    suspend fun removeCartProduct(product: Product.Cart): Int
    suspend fun removeCart()
    suspend fun getCart(): WrapperResults<List<Product.Cart>>

}