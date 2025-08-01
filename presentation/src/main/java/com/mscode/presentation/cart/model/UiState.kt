package com.mscode.presentation.cart.model

sealed class UiState{

    data object Idle: UiState()
    data class DisplayPurchase(val uiCart: List<UiCartProduct>): UiState()

}
