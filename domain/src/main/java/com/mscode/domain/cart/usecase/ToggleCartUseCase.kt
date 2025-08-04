package com.mscode.domain.cart.usecase

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.common.WrapperResults.Error
import com.mscode.domain.common.WrapperResults.Success
import com.mscode.domain.cart.repository.CartRepository
import com.mscode.domain.products.model.Product
import java.lang.Exception

class ToggleCartUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(
        cart: Product.Cart,
        isCart: Boolean
    ): WrapperResults<Unit> = if(isCart) {
        repository.removeCartProduct((cart)).let { ret ->
            if(ret == 0) Error(Exception())
            else Success(Unit)
        }
    } else {
        repository.addCartProduct(cart).let { ret ->
            if(ret == -1L) Error(Exception())
            else Success(Unit)
        }
    }
}