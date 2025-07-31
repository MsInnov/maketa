package com.mscode.presentation.menu.model

sealed class UiState {

    data object Idle : UiState()
    data object Disconnected : UiState()
    data object Favorite : UiState()
    data object Selling : UiState()

}