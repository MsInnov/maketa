package com.mscode.domain.cart.usecase

import com.mscode.domain.common.WrapperResults.Error
import com.mscode.domain.common.WrapperResults.Success
import com.mscode.domain.cart.repository.CartRepository
import com.mscode.domain.products.model.Product
import java.lang.Exception

class RemoveCartProductUseCase (
    private val repository: CartRepository
) {
    suspend operator fun invoke(cartProduct: Product.Cart) = repository.removeCartProduct((cartProduct)).let { ret ->
        if(ret == 0) Error(Exception())
        else Success(Unit)
    }
}