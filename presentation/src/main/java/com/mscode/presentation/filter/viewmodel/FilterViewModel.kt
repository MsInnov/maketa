package com.mscode.presentation.filter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.domain.common.WrapperResults.Success
import com.mscode.domain.favorites.usecase.ToggleFavoriteUseCase
import com.mscode.domain.products.model.Product
import com.mscode.domain.products.usecase.GetCategoryProductsUseCase
import com.mscode.domain.products.usecase.GetProductsByCategoryUseCase
import com.mscode.domain.products.usecase.IsCartFlowUseCase
import com.mscode.domain.products.usecase.SortByPriceAscendingUseCase
import com.mscode.domain.products.usecase.SortByPriceDescendingUseCase
import com.mscode.presentation.filter.model.UiState
import com.mscode.presentation.filter.model.UiEvent
import com.mscode.presentation.home.mapper.ProductsUiMapper
import com.mscode.presentation.home.model.UiProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    private val getCategoryProductsUseCase: GetCategoryProductsUseCase,
    private val sortByPriceAscendingUseCase: SortByPriceAscendingUseCase,
    private val sortByPriceDescendingUseCase: SortByPriceDescendingUseCase,
    private val productsUiMapper: ProductsUiMapper,
    private val isCartFlowUseCase: IsCartFlowUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    private val _uiStateIsDisplayed = MutableStateFlow(false)
    val uiStateIsDisplayed: StateFlow<Boolean> = _uiStateIsDisplayed

    private var isSortByPriceAscending = false
    private var isSortByPriceDescending = false
    private var filterAndSortProductsJob: Job? = null

    init {
        viewModelScope.launch {
            isCartFlowUseCase().collect { isCarts ->
                _uiState.update { state ->
                    when (state) {
                        is UiState.FilteredByCategory -> {
                            state.copy(
                                list = state.list.map { product ->
                                    product.copy(
                                        isCart = isCarts.firstOrNull { it.first == product.id }?.second
                                            ?: false
                                    )
                                }
                            )
                        }

                        else -> state
                    }
                }
            }
        }
    }

    fun onEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                UiEvent.Idle -> {
                    filterAndSortProductsJob?.cancel()
                    _uiState.value = UiState.Idle
                    _uiStateIsDisplayed.value = false
                }

                is UiEvent.FilterByCategory -> filterAndSortProducts(
                    uiEvent.category,
                    uiEvent.listNotSorted
                )

                UiEvent.SortByPriceAscending -> {
                    isSortByPriceDescending = false
                    isSortByPriceAscending = true
                }

                UiEvent.SortByPriceDescending -> {
                    isSortByPriceDescending = true
                    isSortByPriceAscending = false
                }

                UiEvent.GetCategory -> fetchCategoryProducts()
                is UiEvent.UpdateFavorite -> {
                    val updated = toggleFavoriteUseCase(
                        favoriteProducts = productsUiMapper.toFavoriteProduct(uiEvent.products),
                        isFavoriteProducts = uiEvent.products.isFavorite
                    )

                    if (updated is Success && _uiState.value is UiState.FilteredByCategory) {
                        _uiState.update { state ->
                            val currentProducts = (state as UiState.FilteredByCategory).list
                            state.copy(
                                list = currentProducts.map { product ->
                                    if (product.id == uiEvent.products.id) {
                                        product.copy(isFavorite = !product.isFavorite)
                                    } else product
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun fetchCategoryProducts() {
        getCategoryProductsUseCase().apply {
            _uiState.value = UiState.CategoryProducts(list = this)
        }
    }

    private fun filterAndSortProducts(category: String, uiProducts: List<UiProduct>) {
        filterAndSortProductsJob = viewModelScope.launch {
            _uiStateIsDisplayed.value = true
            getProductsByCategoryUseCase(category).collect { products ->
                when {
                    products.isEmpty() && isSortByPriceDescending -> sortByPriceDescending(
                        uiProducts
                    )

                    products.isEmpty() && isSortByPriceAscending -> sortByPriceAscending(uiProducts)
                    products.isNotEmpty() && isSortByPriceDescending -> sortByPriceDescending(
                        products.map { productsUiMapper.toProductUi(it) }
                    )

                    products.isNotEmpty() && isSortByPriceAscending -> sortByPriceAscending(
                        products.map { productsUiMapper.toProductUi(it) }
                    )

                    products.isNotEmpty() -> _uiState.value = UiState.FilteredByCategory(
                        list = products.map { product ->
                            productsUiMapper.toProductUi(
                                product = product,
                                isCart = isCartFlowUseCase().first().firstOrNull {
                                    it.first == product.id
                                }?.second ?: false
                            )
                        }
                    )

                    else -> _uiState.value = UiState.ToHome
                }
            }
            isSortByPriceDescending = false
            isSortByPriceAscending = false
        }
    }

    private suspend fun sortByPriceAscending(uiProducts: List<UiProduct>) {
        sortByPriceAscendingUseCase(productsUiMapper.toProducts(uiProducts)).apply {
            updateFilteredByCategoryState(this)
        }
    }

    private suspend fun sortByPriceDescending(uiProducts: List<UiProduct>) {
        sortByPriceDescendingUseCase(productsUiMapper.toProducts(uiProducts)).apply {
            updateFilteredByCategoryState(this)
        }
    }

    private suspend fun updateFilteredByCategoryState(products: List<Product>) {
        _uiState.value = UiState.FilteredByCategory(
            products.map { product ->
                productsUiMapper.toProductUi(
                    product = product,
                    isCart = isCartFlowUseCase()
                        .first()
                        .firstOrNull {
                            it.first == product.id
                        }?.second ?: false
                )
            }
        )
    }

}