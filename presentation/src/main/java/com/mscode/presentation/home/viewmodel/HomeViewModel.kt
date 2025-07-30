package com.mscode.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.domain.common.WrapperResults.Error
import com.mscode.domain.common.WrapperResults.Success
import com.mscode.domain.products.usecase.GetProductsUseCase
import com.mscode.presentation.home.mapper.ProductsUiMapper
import com.mscode.presentation.home.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val productsUiMapper: ProductsUiMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        observeProducts()
    }

    private fun observeProducts() {
        viewModelScope.launch {
            when (val result = getProductsUseCase()) {
                is Success -> {
                    val mappedProducts = result.data.map(productsUiMapper::toProductsUi)
                    _uiState.value = UiState.Products(products = mappedProducts)
                }
                is Error -> _uiState.value = UiState.Error
            }
        }
    }
}