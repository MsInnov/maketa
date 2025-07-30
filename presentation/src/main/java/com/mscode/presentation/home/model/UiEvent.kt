package com.mscode.presentation.home.model

sealed class UiEvent {

    data object LoadProductsFavorites: UiEvent()
    data object DisplayFavoritesAndPurchases: UiEvent()
    data class UpdateFavorite(val products: UiProducts): UiEvent()
    data class UpdateCart(val products: UiProducts): UiEvent()
    data object EnableCart: UiEvent()
    data object LoadProduct: UiEvent()

}