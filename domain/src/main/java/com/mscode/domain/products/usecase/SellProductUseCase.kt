package com.mscode.domain.products.usecase

import com.mscode.domain.products.model.Product
import com.mscode.domain.products.repository.ProductsRepository

class SellProductUseCase(
    private val repository: ProductsRepository
) {

    suspend operator fun invoke(product: Product) = repository.sellProduct(product)

}