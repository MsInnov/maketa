package com.mscode.presentation.register.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.domain.common.WrapperResults.Error
import com.mscode.domain.common.WrapperResults.Success
import com.mscode.domain.register.usecase.RegisterUseCase
import com.mscode.presentation.register.extension.toUiState
import com.mscode.presentation.register.model.UiEvent
import com.mscode.presentation.register.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    fun onEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            if (uiEvent is UiEvent.Register) {
                when (val result = registerUseCase(uiEvent.pass, uiEvent.email, uiEvent.login)) {
                    is Success -> _uiState.value = UiState.Registered
                    is Error -> _uiState.value = result.exception.toUiState
                }
            }
        }
    }
}