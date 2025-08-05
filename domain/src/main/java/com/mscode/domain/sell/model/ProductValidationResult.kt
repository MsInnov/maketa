package com.mscode.domain.sell.model

data class ProductValidationResult(
    val isValid: Boolean,
    val priceError: Boolean = false,
    val titleError: Boolean = false,
    val descriptionError: Boolean = false,
    val categoryError: Boolean = false,
    val imageUrlError: Boolean = false
)