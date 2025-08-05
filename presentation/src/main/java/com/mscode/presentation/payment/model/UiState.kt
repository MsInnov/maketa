package com.mscode.presentation.payment.model

sealed class UiState {
    data object Idle : UiState()
    data object DisplayBankInfo : UiState()
    data object Validate : UiState()
    data object BankInfoVerified : UiState()
    data class BankInfoError(
        val cardNumberError: Boolean,
        val expiryDateError: Boolean,
        val cardHolderNameError: Boolean,
        val cvvError: Boolean
    ) : UiState()
}
