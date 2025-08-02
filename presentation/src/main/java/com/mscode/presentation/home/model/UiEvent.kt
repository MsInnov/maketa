package com.mscode.presentation.home.model

sealed class UiEvent {

    data object LoadProductsFavorites: UiEvent()
    data object DisplayFavoritesAndCart: UiEvent()
    data class UpdateFavorite(val product: UiProduct): UiEvent()
    data class UpdateCart(val product: UiProduct): UiEvent()
    data object EnableCart: UiEvent()
    data object LoadProduct: UiEvent()

}