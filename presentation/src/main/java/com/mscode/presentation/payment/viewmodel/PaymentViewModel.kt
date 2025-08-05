package com.mscode.presentation.payment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.domain.payment.usecase.VerifyBankInfosUseCase
import com.mscode.presentation.payment.model.UiEvent
import com.mscode.presentation.payment.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val verifyBankInfosUseCase: VerifyBankInfosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState

    fun onEvent(event: UiEvent) {
        viewModelScope.launch {
            when (event) {
                UiEvent.LoadBank -> _uiState.value = UiState.DisplayBankInfo
                UiEvent.Idle -> _uiState.value = UiState.Idle
                UiEvent.Validate -> _uiState.value = UiState.Validate
                is UiEvent.VerifyBankInfo -> {
                    val result = verifyBankInfosUseCase(
                        cardNumber = event.cardNumber,
                        cvv = event.cvv,
                        cardHolderName = event.cardHolderName,
                        expiryDate = event.expiryDate
                    )
                    if (result.isValid) {
                        _uiState.value = UiState.BankInfoVerified
                    } else {
                        _uiState.value = UiState.BankInfoError(
                            cardNumberError = result.cardNumberError,
                            expiryDateError = result.expiryDateError,
                            cardHolderNameError = result.cardHolderNameError,
                            cvvError = result.cvvError
                        )
                    }
                }
            }
        }
    }
}