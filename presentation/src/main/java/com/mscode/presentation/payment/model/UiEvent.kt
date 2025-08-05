package com.mscode.presentation.payment.model

sealed class UiEvent {
    data object Idle : UiEvent()
    data object LoadBank : UiEvent()
    data object Validate : UiEvent()
    data class VerifyBankInfo(
        val cardNumber: String,
        val expiryDate: String,
        val cardHolderName: String,
        val cvv: String
    ): UiEvent()
}