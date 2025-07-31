package com.mscode.presentation.sell.mapper

import com.mscode.domain.products.model.Product
import com.mscode.presentation.sell.model.SellProductUi

class SellProductUiMapper {

    fun toProducts(
        sellProductUi: SellProductUi
    ) = Product(
        id = 0,
        isFavorite = false,
        title = sellProductUi.title,
        image = sellProductUi.image,
        price = sellProductUi.price,
        category = sellProductUi.category,
        description = sellProductUi.description
    )

}