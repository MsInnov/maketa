package com.mscode.presentation.filter.model

import com.mscode.presentation.home.model.UiProduct

sealed class UiEvent {

    data class FilterByCategory(
        val category: String,
        val listNotSorted: List<UiProduct>
    ): UiEvent()
    data object GetCategory: UiEvent()
    data object Idle: UiEvent()
    data object SortByPriceAscending: UiEvent()
    data object SortByPriceDescending: UiEvent()
    data class UpdateFavorite(val products: UiProduct): UiEvent()

}