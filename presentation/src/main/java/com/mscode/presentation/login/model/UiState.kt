package com.mscode.presentation.login.model

sealed class UiState {

    data object Idle : UiState()
    data object Error : UiState()
    data object ErrorPass : UiState()
    data object ErrorLogin : UiState()
    data object Logged : UiState()

}