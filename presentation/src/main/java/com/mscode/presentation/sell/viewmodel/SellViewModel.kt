package com.mscode.presentation.sell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.products.usecase.SellProductUseCase
import com.mscode.presentation.sell.mapper.SellProductUiMapper
import com.mscode.presentation.sell.model.UiEvent
import com.mscode.presentation.sell.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellViewModel @Inject constructor(
    private val sellProductUseCase: SellProductUseCase,
    private val sellProductUiMapper: SellProductUiMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    fun onEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is UiEvent.SellingProduct -> sellProductUseCase(
                    sellProductUiMapper.toProducts(
                        uiEvent.product
                    )
                ).apply {
                    when (this) {
                        is WrapperResults.Success -> _uiState.value = UiState.Success
                        else -> _uiState.value = UiState.Failure
                    }
                }
                UiEvent.Idle -> _uiState.value = UiState.Idle
            }
        }
    }

}