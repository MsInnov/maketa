package com.mscode.data.products.mapper

import com.mscode.data.products.model.ProductEntity
import com.mscode.domain.products.model.Product

class ProductsMapper {

    fun toProducts(
        productEntity: ProductEntity,
        isFavorite: Boolean = false
    ) = Product(
        id = productEntity.id,
        title = productEntity.title,
        price = productEntity.price,
        description = productEntity.description,
        category = productEntity.category,
        image = productEntity.image,
        isFavorite = isFavorite
    )
}