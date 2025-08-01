package com.mscode.domain.products.usecase

import com.mscode.domain.products.repository.ProductsRepository

class IsCartFlowUseCase (
    private val repository: ProductsRepository
) {

    suspend operator fun invoke() = repository.isCartProducts()

}