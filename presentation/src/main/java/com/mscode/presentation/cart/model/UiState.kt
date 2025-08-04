package com.mscode.presentation.cart.model

import com.mscode.presentation.home.model.UiProduct

sealed class UiState{

    data object Idle: UiState()
    data class DisplayCart(val uiCart: List<UiProduct.Cart>): UiState()

}
