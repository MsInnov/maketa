package com.mscode.presentation.sell.mapper

import com.mscode.domain.products.model.Product
import com.mscode.presentation.home.model.UiProduct

class SellProductUiMapper {

    fun toProducts(
        sellProductUi: UiProduct.Sell
    ) = Product.Classic(
        id = 0,
        isFavorite = false,
        title = sellProductUi.title,
        image = sellProductUi.image,
        price = sellProductUi.price,
        category = sellProductUi.category,
        description = sellProductUi.description
    )

}