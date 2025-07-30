package com.mscode.presentation.register.model

sealed class UiEvent {

    data class Register(
        val login: String,
        val pass: String,
        val email: String
    ): UiEvent()

}