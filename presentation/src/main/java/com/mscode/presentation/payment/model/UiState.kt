package com.mscode.presentation.payment.model

sealed class UiState {

    data object Idle: UiState()
    data object DisplayBankInfo: UiState()
    data object Validate: UiState()

}