package com.mscode.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.domain.cart.usecase.ToggleCartUseCase
import com.mscode.domain.common.WrapperResults.Error
import com.mscode.domain.common.WrapperResults.Success
import com.mscode.domain.favorites.usecase.GetAllFavoritesFlowUseCase
import com.mscode.domain.favorites.usecase.GetAllFavoritesUseCase
import com.mscode.domain.favorites.usecase.SaveFavoriteIsDisplayedUseCase
import com.mscode.domain.favorites.usecase.ToggleFavoriteUseCase
import com.mscode.domain.login.usecase.GetTokenUseCase
import com.mscode.domain.products.usecase.GetProductsUseCase
import com.mscode.domain.products.usecase.IsCartFlowUseCase
import com.mscode.presentation.home.mapper.ProductsUiMapper
import com.mscode.presentation.home.model.UiEvent
import com.mscode.presentation.home.model.UiState
import com.mscode.presentation.home.model.UiProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getAllFavoritesUseCase: GetAllFavoritesUseCase,
    private val getAllFavoritesFlowUseCase: GetAllFavoritesFlowUseCase,
    private val toggleCartUseCase: ToggleCartUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val productsUiMapper: ProductsUiMapper,
    private val isCartFlowUseCase: IsCartFlowUseCase,
    private val saveFavoriteIsDisplayed: SaveFavoriteIsDisplayedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _uiStateFavorite = MutableStateFlow(false)
    val uiStateFavorite: StateFlow<Boolean> = _uiStateFavorite

    private val _uiStateFavoriteEnabled = MutableStateFlow(false)
    val uiStateFavoriteEnabled: StateFlow<Boolean> = _uiStateFavoriteEnabled

    private val _uiStateCartEnabled = MutableStateFlow(false)
    val uiStateCartEnabled: StateFlow<Boolean> = _uiStateCartEnabled

    private val _uiStateFavoriteAndCartDisplay = MutableStateFlow(false)
    val uiStateFavoriteAndCartDisplay: StateFlow<Boolean> = _uiStateFavoriteAndCartDisplay

    init {
        getProductsAndObserveCart()
        observeFavorites()
    }

    fun onEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            when (uiEvent) {

                UiEvent.DisplayFavoritesAndCart -> _uiStateFavoriteAndCartDisplay.value = getTokenUseCase() != null

                is UiEvent.UpdateFavorite -> {
                    toggleFavoriteUseCase(
                        favorite = productsUiMapper.toFavorite(uiEvent.product),
                        isFavorite = uiEvent.product.isFavorite
                    ).apply {
                        if (this is Success) {
                            _uiState.update { state ->
                                if (state is UiState.Products) {
                                    val productList = getCurrentProductListForUpdate()
                                    val updatedList = updateFavoriteProductsList(uiEvent.product, productList)
                                    state.copy(products = updatedList)
                                } else state
                            }
                        }
                    }
                }

                is UiEvent.UpdateCart -> {
                    val isInCart = uiEvent.product.isCart
                    toggleCartUseCase(
                        cart = productsUiMapper.toCart(uiEvent.product),
                        isCart = isInCart
                    ).apply {
                        if (this is Success) {
                            _uiState.update { state ->
                                if (state is UiState.Products) {
                                    val updatedProducts = updateCartList(uiEvent.product, state.products)
                                    state.copy(products = updatedProducts)
                                } else state
                            }
                        }
                    }
                }

                UiEvent.LoadFavorites -> {
                    val showFavorites = !_uiStateFavorite.value
                    saveFavoriteIsDisplayed(showFavorites)
                    _uiStateFavorite.value = showFavorites

                    _uiState.value = if (showFavorites) {
                        buildFavoriteProductsUiState()
                    } else {
                        buildAllProductsUiState()
                    }
                }

                UiEvent.EnableCart -> _uiState.value.apply {
                    if(this is UiState.Products) {
                        _uiStateCartEnabled.value =
                            this.products.firstOrNull { it.isCart } != null
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
                                    productsUiMapper.toProductUi(
                                        product = product,
                                        isCart = isCartFlowUseCase().first().firstOrNull { it.first == product.id }?.second ?: false
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
            products = favorites.map { productsUiMapper.toProductUi(it) }
        )
    }

    private suspend fun buildAllProductsUiState(): UiState {
        return when (val result = getProductsUseCase()) {
            is Success -> {
                UiState.Products(
                    products = result.data.map { product ->
                        productsUiMapper.toProductUi(
                            product = product
                        )
                    }
                )
            }
            is Error -> UiState.Error
        }
    }

    private fun updateFavoriteProductsList(
        uiProduct: UiProduct.Classic,
        currentList: List<UiProduct.Classic>
    ): List<UiProduct.Classic> {
        return currentList.map {
            if (it.id == uiProduct.id) {
                it.copy(isFavorite = !uiProduct.isFavorite)
            } else {
                it
            }
        }
    }

    private fun updateCartList(
        productToUpdate: UiProduct.Classic,
        products: List<UiProduct.Classic>
    ): List<UiProduct.Classic> {
        return products.map { product ->
            if (product.id == productToUpdate.id) {
                product.copy(isCart = !productToUpdate.isCart)
            } else {
                product
            }
        }
    }

    private suspend fun getCurrentProductListForUpdate(): List<UiProduct.Classic> {
        return if (_uiStateFavorite.value) {
            getAllFavoritesUseCase().map(productsUiMapper::toProductUi)
        } else {
            (_uiState.value as? UiState.Products)?.products ?: emptyList()
        }
    }


    private fun getProductsAndObserveCart() {
        viewModelScope.launch {
            when (val result = getProductsUseCase()) {
                is Success -> {
                    val mappedProducts = result.data.map(productsUiMapper::toProductUi)
                    _uiState.value = UiState.Products(products = mappedProducts)

                    observeCartChanges()
                }
                is Error -> _uiState.value = UiState.Error
            }
        }
    }

    private fun observeCartChanges() {
        viewModelScope.launch {
            isCartFlowUseCase().collect { cartList ->
                _uiState.update { state ->
                    if (state is UiState.Products) {
                        state.copy(
                            products = state.products.map { product ->
                                val isInCart =
                                    cartList.firstOrNull { it.first == product.id }?.second ?: false
                                product.copy(isCart = isInCart)
                            }
                        )
                    } else state
                }
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getAllFavoritesFlowUseCase().collect { favorites ->
                val hasFavorites = favorites.isNotEmpty()
                _uiStateFavoriteEnabled.value = hasFavorites

                if (!hasFavorites && uiStateFavorite.value) {
                    onEvent(UiEvent.LoadFavorites)
                }
            }
        }
    }
}