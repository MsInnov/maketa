package com.mscode.presentation.cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.presentation.cart.model.UiEvent
import com.mscode.presentation.cart.model.UiState
import com.mscode.domain.cart.usecase.GetCartUseCase
import com.mscode.domain.cart.usecase.RemoveCartProductUseCase
import com.mscode.domain.cart.usecase.RemoveCartUseCase
import com.mscode.domain.common.WrapperResults
import com.mscode.presentation.cart.mapper.CartProductUiMapper
import com.mscode.presentation.cart.model.UiCartProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartsUseCase: GetCartUseCase,
    private val removeCartProductUseCase: RemoveCartProductUseCase,
    private val removeCartUseCase: RemoveCartUseCase,
    private val cartProductUiMapper: CartProductUiMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState

    fun onEvent(event: UiEvent) {
        viewModelScope.launch {
            when (event) {
                UiEvent.DeleteAllCarts -> removeCartUseCase()

                UiEvent.GetCarts -> getCart()

                is UiEvent.DeleteItem -> deleteCart(event.uiCartProduct)
            }
        }
    }

    private suspend fun getCart() {
        when (val result = getCartsUseCase()) {
            is WrapperResults.Success -> {
                val uiCartList = result.data.map { cartProductUiMapper.toUiCartProduct(it) }
                _uiState.value = UiState.DisplayPurchase(uiCartList)
            }
            else -> Unit
        }
    }

    private suspend fun deleteCart(uiCartProduct: UiCartProduct) {
        val certProduct = cartProductUiMapper.toCartProduct(uiCartProduct)
        when (removeCartProductUseCase(certProduct)) {
            is WrapperResults.Success -> getCart()
            else -> Unit
        }
    }

}