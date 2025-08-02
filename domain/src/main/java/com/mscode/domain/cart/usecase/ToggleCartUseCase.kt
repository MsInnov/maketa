package com.mscode.domain.cart.usecase

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.common.WrapperResults.Error
import com.mscode.domain.common.WrapperResults.Success
import com.mscode.domain.cart.model.CartProduct
import com.mscode.domain.cart.repository.CartRepository
import java.lang.Exception

class ToggleCartUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(
        cartProduct: CartProduct,
        isCartProducts: Boolean
    ): WrapperResults<Unit> = if(isCartProducts) {
        repository.removeCartProduct((cartProduct)).let { ret ->
            if(ret == 0) Error(Exception())
            else Success(Unit)
        }
    } else {
        repository.addCartProduct(cartProduct).let { ret ->
            if(ret == -1L) Error(Exception())
            else Success(Unit)
        }
    }
}