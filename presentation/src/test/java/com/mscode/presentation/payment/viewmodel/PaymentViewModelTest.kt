package com.mscode.presentation.payment.viewmodel

import app.cash.turbine.test
import com.mscode.domain.payment.model.BankInfoValidationResult
import com.mscode.domain.payment.usecase.VerifyBankInfosUseCase
import com.mscode.presentation.payment.model.UiEvent
import com.mscode.presentation.payment.model.UiEvent.LoadBank
import com.mscode.presentation.payment.model.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentViewModelTest {

    private val verifyBankInfosUseCase: VerifyBankInfosUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: PaymentViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PaymentViewModel(verifyBankInfosUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEvent LoadBank should update uiState to DisplayBankInfo`() = runTest {
        // Given
        // When
        viewModel.onEvent(LoadBank)
        testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(UiState.DisplayBankInfo, viewModel.uiState.value)
    }

    @Test
    fun `onEvent Idle should update uiState to Idle`() = runTest {
        // Given
        // When
        viewModel.onEvent(UiEvent.Idle)
        testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(UiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `onEvent Validate should update uiState to Validate`() = runTest {
        // Given
        // When
        viewModel.onEvent(UiEvent.Validate)
        testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(UiState.Validate, viewModel.uiState.value)
    }

    @Test
    fun `onEvent VerifyBankInfos and is Valid should update uiState to BankInfoVerified`() = runTest {
        // Given
        val cardNumber = "1111111111111111"
        val cvv = "123"
        val cardHolderName = "jean"
        val expiryDate = "12/25"
        coEvery {
            verifyBankInfosUseCase(
                cardNumber = cardNumber,
                cvv = cvv,
                cardHolderName = cardHolderName,
                expiryDate = expiryDate)
        } returns BankInfoValidationResult(
            isValid = true,
            cardHolderNameError = true,
            cvvError = true,
            cardNumberError = true,
            expiryDateError = true
        )
        // When
        viewModel.onEvent(UiEvent.VerifyBankInfo(cardNumber, expiryDate, cardHolderName, cvv))

        // Then
        viewModel.uiState.test {
            assertEquals(UiState.Idle, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.BankInfoVerified, awaitItem())
        }

        coVerify(exactly = 1) {
            verifyBankInfosUseCase(
                cardNumber = cardNumber,
                cvv = cvv,
                cardHolderName = cardHolderName,
                expiryDate = expiryDate
            )
        }
    }

    @Test
    fun `onEvent VerifyBankInfos and not valid should update uiState to BankInfoError`() = runTest {
        // Given
        val cardNumber = "1111111111111111"
        val cvv = "123"
        val cardHolderName = "jean"
        val expiryDate = "12/25"
        coEvery {
            verifyBankInfosUseCase(
                cardNumber = cardNumber,
                cvv = cvv,
                cardHolderName = cardHolderName,
                expiryDate = expiryDate)
        } returns BankInfoValidationResult(
            isValid = false,
            cardHolderNameError = false,
            cvvError = true,
            cardNumberError = true,
            expiryDateError = true
        )
        // When
        viewModel.onEvent(UiEvent.VerifyBankInfo(cardNumber, expiryDate, cardHolderName, cvv))

        // Then
        viewModel.uiState.test {
            assertEquals(UiState.Idle, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(
                UiState.BankInfoError(
                    cardHolderNameError = false,
                    cvvError = true,
                    cardNumberError = true,
                    expiryDateError = true
                ),
                awaitItem()
            )
        }

        coVerify(exactly = 1) {
            verifyBankInfosUseCase(
                cardNumber = cardNumber,
                cvv = cvv,
                cardHolderName = cardHolderName,
                expiryDate = expiryDate
            )
        }
    }
}