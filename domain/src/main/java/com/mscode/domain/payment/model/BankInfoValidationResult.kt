package com.mscode.domain.payment.model

data class BankInfoValidationResult(
    val isValid: Boolean,
    val cardNumberError: Boolean = false,
    val expiryDateError: Boolean = false,
    val cardHolderNameError: Boolean = false,
    val cvvError: Boolean = false
)
