package com.mscode.presentation.account.model

sealed class UiEvent {

    data object GetProfile: UiEvent()

}