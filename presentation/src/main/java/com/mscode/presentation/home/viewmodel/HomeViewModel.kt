package com.mscode.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.domain.common.WrapperResults.Error
import com.mscode.domain.common.WrapperResults.Success
import com.mscode.domain.favorites.usecase.GetAllFavoritesFlowUseCase
import com.mscode.domain.favorites.usecase.GetAllFavoritesUseCase
import com.mscode.domain.favorites.usecase.SaveFavoriteIsDisplayedUseCase
import com.mscode.domain.favorites.usecase.ToggleFavoriteUseCase
import com.mscode.domain.login.usecase.GetTokenUseCase
import com.mscode.domain.products.usecase.GetProductsUseCase
import com.mscode.presentation.home.mapper.ProductsUiMapper
import com.mscode.presentation.home.model.UiEvent
import com.mscode.presentation.home.model.UiState
import com.mscode.presentation.home.model.UiProducts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getAllFavoritesUseCase: GetAllFavoritesUseCase,
    private val getAllFavoritesFlowUseCase: GetAllFavoritesFlowUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val productsUiMapper: ProductsUiMapper,
    private val saveFavoriteIsDisplayed: SaveFavoriteIsDisplayedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _uiStateFavorite = MutableStateFlow(false)
    val uiStateFavorite: StateFlow<Boolean> = _uiStateFavorite

    private val _uiStateFavoriteEnabled = MutableStateFlow(false)
    val uiStateFavoriteEnabled: StateFlow<Boolean> = _uiStateFavoriteEnabled

    private val _uiStateFavoriteDisplay = MutableStateFlow(false)
    val uiStateFavoriteDisplay: StateFlow<Boolean> = _uiStateFavoriteDisplay

    init {
        getProducts()
        observeFavorites()
    }

    fun onEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            when (uiEvent) {

                UiEvent.DisplayFavorites -> _uiStateFavoriteDisplay.value = getTokenUseCase() != null

                is UiEvent.UpdateFavorite -> {
                    toggleFavoriteUseCase(
                        favoriteProducts = productsUiMapper.toFavoriteProducts(uiEvent.products),
                        isFavoriteProducts = uiEvent.products.isFavorite
                    ).apply {
                        if (this is Success) {
                            _uiState.update { state ->
                                if (state is UiState.Products) {
                                    val productList = getCurrentProductListForUpdate()
                                    val updatedList = updateFavoriteProductsList(uiEvent.products, productList)
                                    state.copy(products = updatedList)
                                } else state
                            }
                        }
                    }
                }

                UiEvent.LoadProductsFavorites -> {
                    val showFavorites = !_uiStateFavorite.value
                    saveFavoriteIsDisplayed(showFavorites)
                    _uiStateFavorite.value = showFavorites

                    _uiState.value = if (showFavorites) {
                        buildFavoriteProductsUiState()
                    } else {
                        buildAllProductsUiState()
                    }
                }

                UiEvent.LoadProduct -> getProductsUseCase().apply {
                    when (this) {
                        is Success -> {
                            if(_uiStateFavorite.value) {
                                saveFavoriteIsDisplayed(false)
                                _uiStateFavorite.value = false
                            }
                            _uiState.value = UiState.Products(
                                products = data.map { product ->
                                    productsUiMapper.toProductsUi(
                                        products = product
                                    )
                                },
                            )
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private suspend fun buildFavoriteProductsUiState(): UiState {
        val favorites = getAllFavoritesUseCase()
        return UiState.Products(
            products = favorites.map { productsUiMapper.toProductsUi(it) }
        )
    }

    private suspend fun buildAllProductsUiState(): UiState {
        return when (val result = getProductsUseCase()) {
            is Success -> {
                UiState.Products(
                    products = result.data.map { product ->
                        productsUiMapper.toProductsUi(
                            products = product
                        )
                    }
                )
            }
            is Error -> UiState.Error
        }
    }

    private fun updateFavoriteProductsList(
        uiProduct: UiProducts,
        currentList: List<UiProducts>
    ): List<UiProducts> {
        return currentList.map {
            if (it.id == uiProduct.id) {
                it.copy(isFavorite = !uiProduct.isFavorite)
            } else {
                it
            }
        }
    }

    private suspend fun getCurrentProductListForUpdate(): List<UiProducts> {
        return if (_uiStateFavorite.value) {
            getAllFavoritesUseCase().map(productsUiMapper::toProductsUi)
        } else {
            (_uiState.value as? UiState.Products)?.products ?: emptyList()
        }
    }


    private fun getProducts() {
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

    private fun observeFavorites() {
        viewModelScope.launch {
            getAllFavoritesFlowUseCase().collect { favorites ->
                val hasFavorites = favorites.isNotEmpty()
                _uiStateFavoriteEnabled.value = hasFavorites

                if (!hasFavorites && uiStateFavorite.value) {
                    onEvent(UiEvent.LoadProductsFavorites)
                }
            }
        }
    }
}