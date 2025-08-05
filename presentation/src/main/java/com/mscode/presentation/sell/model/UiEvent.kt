package com.mscode.presentation.sell.model

import com.mscode.presentation.home.model.UiProduct

sealed class UiEvent {

    data object Idle: UiEvent()
    data class SellingProduct(
        val product: UiProduct.Sell
    ): UiEvent()
    data class VerifyFormValid(
        val title: String,
        val description: String,
        val price: String,
        val category: String,
        val imageUrl: String
    ): UiEvent()
}