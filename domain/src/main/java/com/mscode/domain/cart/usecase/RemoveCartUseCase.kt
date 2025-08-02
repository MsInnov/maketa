package com.mscode.domain.cart.usecase
import com.mscode.domain.cart.repository.CartRepository

class RemoveCartUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke() = repository.removeCart()
}