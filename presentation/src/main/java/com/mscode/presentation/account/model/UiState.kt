package com.mscode.presentation.account.model

sealed class UiState {

    data object Idle: UiState()
    data class Profile(
        val username: String,
        val email: String
    ): UiState()
    data object Error: UiState()

}