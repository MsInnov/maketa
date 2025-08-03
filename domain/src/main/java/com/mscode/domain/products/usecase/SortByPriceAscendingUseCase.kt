package com.mscode.domain.products.usecase

import com.mscode.domain.products.model.Product

class SortByPriceAscendingUseCase {

    operator fun invoke(products: List<Product>) = products.sortedBy { it.price }

}