package com.mscode.presentation.cart.model

sealed class UiEvent{
    data object GetCarts : UiEvent()
    data class DeleteItem(val uiCartProduct: UiCartProduct) : UiEvent()
    data object DeleteAllCarts : UiEvent()
}
