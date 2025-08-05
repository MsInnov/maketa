package com.mscode.domain.payment.usecase

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VerifyBankInfosUseCaseTest {

    private lateinit var useCase: VerifyBankInfosUseCase

    @BeforeEach
    fun setUp() {
        useCase = VerifyBankInfosUseCase()
    }

    @Test
    fun `should return valid when all fields are correct`() {
        val result = useCase(
            cardNumber = "1234567812345678",
            expiryDate = "12/30",
            cardHolderName = "John Doe",
            cvv = "123"
        )

        assertTrue(result.isValid)
        assertFalse(result.cardNumberError)
        assertFalse(result.expiryDateError)
        assertFalse(result.cardHolderNameError)
        assertFalse(result.cvvError)
    }

    @Test
    fun `should return error for invalid card number`() {
        val result = useCase(
            cardNumber = "1234",
            expiryDate = "12/30",
            cardHolderName = "John Doe",
            cvv = "123"
        )

        assertFalse(result.isValid)
        assertTrue(result.cardNumberError)
        assertFalse(result.expiryDateError)
        assertFalse(result.cardHolderNameError)
        assertFalse(result.cvvError)
    }

    @Test
    fun `should return error for expired card`() {
        val result = useCase(
            cardNumber = "1234567812345678",
            expiryDate = "01/20", // date dans le passé
            cardHolderName = "John Doe",
            cvv = "123"
        )

        assertFalse(result.isValid)
        assertTrue(result.expiryDateError)
    }

    @Test
    fun `should return error for empty card holder name`() {
        val result = useCase(
            cardNumber = "1234567812345678",
            expiryDate = "12/30",
            cardHolderName = "   ",
            cvv = "123"
        )

        assertFalse(result.isValid)
        assertTrue(result.cardHolderNameError)
    }

    @Test
    fun `should return error for invalid cvv`() {
        val result = useCase(
            cardNumber = "1234567812345678",
            expiryDate = "12/30",
            cardHolderName = "John Doe",
            cvv = "1" // trop court
        )

        assertFalse(result.isValid)
        assertTrue(result.cvvError)
    }

    @Test
    fun `should return multiple errors`() {
        val result = useCase(
            cardNumber = "1234",
            expiryDate = "13/99", // mois invalide
            cardHolderName = "",
            cvv = "1"
        )

        assertFalse(result.isValid)
        assertTrue(result.cardNumberError)
        assertTrue(result.expiryDateError)
        assertTrue(result.cardHolderNameError)
        assertTrue(result.cvvError)
    }
}
