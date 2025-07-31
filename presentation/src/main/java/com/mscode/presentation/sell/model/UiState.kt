package com.mscode.presentation.sell.model

sealed class UiState {

    data object Idle: UiState()
    data object Success: UiState()
    data object Failure: UiState()

}