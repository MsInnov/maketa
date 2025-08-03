package com.mscode.presentation.filter.model

import com.mscode.presentation.home.model.UiProduct

sealed class UiState {

    data object Idle: UiState()
    data class CategoryProducts(val list: List<String>): UiState()
    data class FilteredByCategory(val list: List<UiProduct>): UiState()
    data object ToHome: UiState()
}