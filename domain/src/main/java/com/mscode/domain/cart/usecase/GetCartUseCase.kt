package com.mscode.domain.cart.usecase

import com.mscode.domain.cart.repository.CartRepository

class GetCartUseCase(
    private val repository: CartRepository
) {

    suspend operator fun invoke() = repository.getCart()

}