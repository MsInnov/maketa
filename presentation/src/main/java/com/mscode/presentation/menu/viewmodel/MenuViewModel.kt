package com.mscode.presentation.menu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.domain.login.usecase.RemoveTokenUseCase
import com.mscode.presentation.menu.model.UiEvent
import com.mscode.presentation.menu.model.UiEvent.Disconnect
import com.mscode.presentation.menu.model.UiEvent.Favorite
import com.mscode.presentation.menu.model.UiEvent.Idle
import com.mscode.presentation.menu.model.UiState
import com.mscode.presentation.menu.model.UiState.Disconnected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val removeTokenUseCase: RemoveTokenUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    fun onEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                Disconnect -> {
                    removeTokenUseCase()
                    _uiState.value = Disconnected
                }
                Favorite -> _uiState.value = UiState.Favorite
                Idle -> _uiState.value = UiState.Idle
            }
        }
    }
}