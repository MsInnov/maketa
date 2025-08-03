package com.mscode.domain.products.usecase

import com.mscode.domain.products.repository.ProductsRepository

class GetCategoryProductsUseCase(
    private val repository: ProductsRepository
) {

    companion object{
        private const val NONE = "NONE"
    }

    operator fun invoke() = repository.getCategoryProducts()
        .toMutableList()
        .apply { add(0, NONE) }
        .toList()

}