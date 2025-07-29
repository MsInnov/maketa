package com.mscode.presentation.splashscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.presentation.splashscreen.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Pending)
    val uiState = _uiState

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady

    init {
        initRemoteConfig()
    }

    private fun initRemoteConfig() = viewModelScope.launch {
        viewModelScope.launch {
            delay(3000)
            _uiState.value = UiState.Success
            _isReady.value = true
        }
    }

}