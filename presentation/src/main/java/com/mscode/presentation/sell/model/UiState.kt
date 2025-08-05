package com.mscode.presentation.sell.model

sealed class UiState {

    data object Idle: UiState()
    data object Success: UiState()
    data class VerifyFormValid(
        val isValid: Boolean,
        val priceError: Boolean = false,
        val titleError: Boolean = false,
        val descriptionError: Boolean = false,
        val categoryError: Boolean = false,
        val imageUrlError: Boolean = false
    ): UiState()
    data object Failure: UiState()

}