package com.mscode.presentation.splashscreen.model

sealed class UiState {
    data object Pending : UiState()
    data object Success : UiState()
    data object Error : UiState()
}