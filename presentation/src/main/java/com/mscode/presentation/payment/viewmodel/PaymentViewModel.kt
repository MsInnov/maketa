package com.mscode.presentation.payment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.presentation.payment.model.UiEvent
import com.mscode.presentation.payment.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState

    fun onEvent(event: UiEvent) {
        viewModelScope.launch {
            when (event) {
                UiEvent.LoadBank -> _uiState.value = UiState.DisplayBankInfo
                UiEvent.Idle -> _uiState.value = UiState.Idle
                UiEvent.Validate -> _uiState.value = UiState.Validate
            }
        }
    }
}