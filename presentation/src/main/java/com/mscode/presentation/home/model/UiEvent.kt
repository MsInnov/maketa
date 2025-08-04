package com.mscode.presentation.home.model

sealed class UiEvent {

    data object LoadFavorites: UiEvent()
    data object DisplayFavoritesAndCart: UiEvent()
    data class UpdateFavorite(val product: UiProduct.Classic): UiEvent()
    data class UpdateCart(val product: UiProduct.Classic): UiEvent()
    data object EnableCart: UiEvent()
    data object LoadProduct: UiEvent()

}