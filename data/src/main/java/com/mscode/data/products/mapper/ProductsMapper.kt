package com.mscode.data.products.mapper

import com.mscode.data.products.model.ProductEntity
import com.mscode.domain.products.model.Product

class ProductsMapper {

    fun toProducts(
        productEntity: ProductEntity,
        isFavorite: Boolean
    ) = Product.Classic(
        id = productEntity.id,
        title = productEntity.title,
        price = productEntity.price,
        description = productEntity.description,
        category = productEntity.category,
        image = productEntity.image,
        isFavorite = isFavorite
    )

    fun toProductEntity(
        product: Product.Classic
    ) = ProductEntity(
        id = product.id,
        title = product.title,
        price = product.price,
        description = product.description,
        category = product.category,
        image = product.image
    )
}