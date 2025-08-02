package com.mscode.domain.cart.usecase

import com.mscode.domain.common.WrapperResults.Error
import com.mscode.domain.common.WrapperResults.Success
import com.mscode.domain.cart.model.CartProduct
import com.mscode.domain.cart.repository.CartRepository
import java.lang.Exception

class RemoveCartProductUseCase (
    private val repository: CartRepository
) {
    suspend operator fun invoke(purchaseProduct: CartProduct) = repository.removeCartProduct((purchaseProduct)).let { ret ->
        if(ret == 0) Error(Exception())
        else Success(Unit)
    }
}