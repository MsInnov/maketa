package com.mscode.data.products.mapper

import com.mscode.data.products.model.ProductEntity
import com.mscode.domain.products.model.Product

class ProductsMapper {

    fun toProducts(
        productEntity: ProductEntity,
        isFavorite: Boolean
    ) = Product(
        id = productEntity.id,
        title = productEntity.title,
        price = productEntity.price,
        description = productEntity.description,
        category = productEntity.category,
        image = productEntity.image,
        isFavorite = isFavorite
    )

    fun toProductEntity(
        products: Product
    ) = ProductEntity(
        id = products.id,
        title = products.title,
        price = products.price,
        description = products.description,
        category = products.category,
        image = products.image
    )
}