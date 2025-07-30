package com.mscode.presentation.login.model

sealed class UiEvent {

    data class Login(
        val login: String,
        val pass: String
    ): UiEvent()
    data object Disconnect: UiEvent()

}