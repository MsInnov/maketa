package com.mscode.domain.products.usecase

import com.mscode.domain.products.repository.ProductsRepository

class GetProductsUseCase(
    private val repository: ProductsRepository
) {

    suspend operator fun invoke() = repository.getProducts()

}