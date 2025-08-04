package com.mscode.presentation.cart.model

import com.mscode.presentation.home.model.UiProduct

sealed class UiEvent{
    data object GetCarts : UiEvent()
    data class DeleteItem(val uiCart: UiProduct.Cart) : UiEvent()
    data object DeleteAllCarts : UiEvent()
}
