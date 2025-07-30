package com.mscode.presentation.register.model

sealed class UiState {

    data object Idle : UiState()
    data object Error : UiState()
    data object ErrorPass : UiState()
    data object ErrorEmail : UiState()
    data object ErrorLogin : UiState()
    data object Registered : UiState()

}