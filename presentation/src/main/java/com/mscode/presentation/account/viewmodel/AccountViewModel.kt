package com.mscode.presentation.account.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.domain.user.usecase.GetUserUseCase
import com.mscode.domain.common.WrapperResults
import com.mscode.presentation.account.model.UiEvent
import com.mscode.presentation.account.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    fun onEvent(event: UiEvent) {
        viewModelScope.launch {
            when(event) {
                UiEvent.GetProfile -> when(val user = getUserUseCase()){
                    is WrapperResults.Success -> _uiState.value = UiState.Profile(
                        username = user.data.user,
                        email = user.data.email
                    )
                    else -> _uiState.value = UiState.Error
                }
            }
        }
    }

}