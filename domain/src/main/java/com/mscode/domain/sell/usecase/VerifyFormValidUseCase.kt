package com.mscode.domain.sell.usecase

import com.mscode.domain.sell.model.ProductValidationResult

class VerifyFormValidUseCase {

    operator fun invoke(
        title: String,
        description: String,
        price: String,
        category: String,
        imageUrl: String
    ): ProductValidationResult {
        val titleError = title.isBlank()
        val descriptionError = description.isBlank()
        val categoryError = category.isBlank()
        val imageUrlError = imageUrl.isBlank()
        val priceValue = price.toDoubleOrNull()
        val priceError = price.isBlank() || priceValue == null || priceValue < 0.0

        val hasError = titleError || descriptionError || categoryError || imageUrlError || priceError

        return ProductValidationResult(
            isValid = !hasError,
            titleError = titleError,
            descriptionError = descriptionError,
            categoryError = categoryError,
            imageUrlError = imageUrlError,
            priceError = priceError
        )
    }

}