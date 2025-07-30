package com.mscode.presentation.splashscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.remoteconfig.usecase.GetRemoteConfigUseCase
import com.mscode.presentation.splashscreen.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val getRemoteConfigUseCase: GetRemoteConfigUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Pending)
    val uiState = _uiState

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady

    private var delayMinimumDawn = false

    init {
        initRemoteConfig()
    }

    private fun initRemoteConfig() = viewModelScope.launch {
        viewModelScope.launch {
            delay(3000)
            if(_uiState.value == UiState.Success) _isReady.value = true
            delayMinimumDawn = true
        }
        getRemoteConfigUseCase().apply{
            when(this){
                is WrapperResults.Success -> {
                    if(delayMinimumDawn) _isReady.value = true
                    _uiState.value = UiState.Success
                }
                else -> _uiState.value = UiState.Error
            }
        }
    }

}