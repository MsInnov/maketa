package com.mscode.presentation.payment.model

data class BankInfo(
    val type: CardType,
    val number: String,
    val expiry: String,
    val holderName: String,
    val cvv: String
)