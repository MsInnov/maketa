package com.mscode.domain.payment.usecase

import com.mscode.domain.payment.model.BankInfoValidationResult

class VerifyBankInfosUseCase {

    operator fun invoke(
        cardNumber: String,
        expiryDate: String,
        cardHolderName: String,
        cvv: String
    ): BankInfoValidationResult {
        val cardNumberError = !cardNumber.matches(Regex("\\d{16}"))
        val expiryDateError = !expiryDate.matches(Regex("(0[1-9]|1[0-2])/\\d{2}")) || !isExpiryValid(expiryDate)
        val cardHolderNameError = cardHolderName.trim().isEmpty()
        val cvvError = !cvv.matches(Regex("\\d{3}"))

        val hasError = cardNumberError || expiryDateError || cardHolderNameError || cvvError

        return BankInfoValidationResult(
            isValid = !hasError,
            cardNumberError = cardNumberError,
            expiryDateError = expiryDateError,
            cardHolderNameError = cardHolderNameError,
            cvvError = cvvError
        )
    }

    private fun isExpiryValid(expiry: String): Boolean {
        val parts = expiry.split("/")
        if (parts.size != 2) return false

        val (monthStr, yearStr) = parts
        val month = monthStr.toIntOrNull()
        val year = yearStr.toIntOrNull()

        if (month == null || year == null || month !in 1..12) return false

        val fullYear = if (year < 100) 2000 + year else year
        val expiryValue = fullYear * 100 + month

        val current = java.util.Calendar.getInstance()
        val currentYear = current.get(java.util.Calendar.YEAR)
        val currentMonth = current.get(java.util.Calendar.MONTH) + 1

        val currentValue = currentYear * 100 + currentMonth

        return expiryValue >= currentValue
    }
}
