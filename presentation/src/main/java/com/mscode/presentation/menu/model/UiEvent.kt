package com.mscode.presentation.menu.model

sealed class UiEvent {

    data object Disconnect: UiEvent()
    data object Idle: UiEvent()

}