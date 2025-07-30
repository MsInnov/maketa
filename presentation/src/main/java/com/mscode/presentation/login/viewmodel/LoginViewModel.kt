package com.mscode.presentation.login.viewmodel

import com.mscode.domain.login.usecase.LoginUseCase
import com.mscode.presentation.login.extension.toUiState
import com.mscode.presentation.login.model.UiEvent
import com.mscode.presentation.login.model.UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.domain.common.WrapperResults.Error
import com.mscode.domain.common.WrapperResults.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    fun onEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            if (uiEvent is UiEvent.Login) {
                when (val result = loginUseCase(uiEvent.login, uiEvent.pass)) {
                    is Success -> _uiState.value = UiState.Logged
                    is Error -> _uiState.value = result.exception.toUiState
                }
            }
            if (uiEvent is UiEvent.Disconnect) {
                _uiState.value = UiState.Idle
            }
        }
    }

}