package com.mscode.presentation.register.extension

import com.mscode.domain.login.model.LoginError
import com.mscode.presentation.register.model.UiState

val Exception.toUiState get() = when (this) {
    is LoginError.ErrorPass -> UiState.ErrorPass
    is LoginError.ErrorLogin -> UiState.ErrorLogin
    else -> UiState.Error
}
