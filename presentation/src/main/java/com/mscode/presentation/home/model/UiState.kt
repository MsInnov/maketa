package com.mscode.presentation.home.model

sealed class UiState {

    data object Loading : UiState()
    data object Error : UiState()
    data class Products(val products: List<UiProduct>) : UiState()

}