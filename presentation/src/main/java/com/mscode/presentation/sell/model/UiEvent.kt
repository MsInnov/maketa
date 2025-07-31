package com.mscode.presentation.sell.model

sealed class UiEvent {

    data object Idle: UiEvent()
    data class SellingProduct(
        val product: SellProductUi
    ): UiEvent()

}