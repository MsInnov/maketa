package com.mscode.domain.products.usecase

import com.mscode.domain.products.model.Product

class SortByPriceDescendingUseCase {

    operator fun invoke(products: List<Product>) = products.sortedByDescending { it.price }

}