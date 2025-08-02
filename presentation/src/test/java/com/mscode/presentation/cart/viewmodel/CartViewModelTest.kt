package com.mscode.presentation.cart.viewmodel

import app.cash.turbine.test
import com.mscode.domain.cart.model.CartProduct
import com.mscode.domain.cart.usecase.GetCartUseCase
import com.mscode.domain.cart.usecase.RemoveCartProductUseCase
import com.mscode.domain.cart.usecase.RemoveCartUseCase
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.favorites.model.FavoriteProduct
import com.mscode.domain.products.model.Product
import com.mscode.presentation.cart.mapper.CartProductUiMapper
import com.mscode.presentation.cart.model.UiCartProduct
import com.mscode.presentation.cart.model.UiEvent
import com.mscode.presentation.cart.model.UiState
import com.mscode.presentation.home.model.UiProduct
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    private val getCartsUseCase: GetCartUseCase = mockk()
    private val removeCartProductUseCase: RemoveCartProductUseCase = mockk()
    private val removeCartUseCase: RemoveCartUseCase = mockk()
    private val cartProductUiMapper: CartProductUiMapper = mockk()
    private lateinit var viewModel: CartViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val product = Product(1, "Title", 10.0, "Desc", "Cat", "Img", false)
    private val favoriteProducts = FavoriteProduct(1, "Title", 10.0, "Desc", "Cat", "Img", false)
    private val uiProduct = UiProduct(1, "Title", 10.0, "Desc", "Cat", "Img", false, false)
    private val cartProduct = CartProduct(1, "Title", 10.0, "Desc", "Cat", "Img")
    private val uiCartProduct = UiCartProduct(1, "Title", 10.0, "Desc", "Cat", "Img")


    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should called removeCartUseCase when DeleteAllCarts uiEvent`() = runTest {
        // Given
        coEvery { removeCartUseCase() } returns Unit
        // When
        viewModel = CartViewModel(
            getCartsUseCase,
            removeCartProductUseCase,
            removeCartUseCase,
            cartProductUiMapper
        )
        viewModel.onEvent(UiEvent.DeleteAllCarts)
        advanceUntilIdle()
        // Then
        coVerify{ removeCartUseCase() }
    }

    @Test
    fun `should emit DisplayPurchase UiState when GetCarts uiEvent and getCartsUseCase success`() = runTest {
        // Given
        coEvery { getCartsUseCase() } returns WrapperResults.Success(listOf(cartProduct))
        coEvery { cartProductUiMapper.toUiCartProduct(cartProduct) } returns uiCartProduct
        // When
        viewModel = CartViewModel(
            getCartsUseCase,
            removeCartProductUseCase,
            removeCartUseCase,
            cartProductUiMapper
        )
        viewModel.onEvent(UiEvent.GetCarts)
        // Then
        viewModel.uiState.test {
            assertEquals(UiState.Idle, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.DisplayPurchase(listOf(uiCartProduct)), awaitItem())
        }
    }

    @Test
    fun `should emit DisplayPurchase UiState when DeleteItem uiEvent and removeCartProductUseCase success`() = runTest {
        // Given
        coEvery { getCartsUseCase() } returns WrapperResults.Success(listOf(cartProduct))
        coEvery { cartProductUiMapper.toCartProduct(uiCartProduct) } returns cartProduct
        coEvery { cartProductUiMapper.toUiCartProduct(cartProduct) } returns uiCartProduct
        coEvery { removeCartProductUseCase(cartProduct) } returns WrapperResults.Success(Unit)
        // When
        viewModel = CartViewModel(
            getCartsUseCase,
            removeCartProductUseCase,
            removeCartUseCase,
            cartProductUiMapper
        )
        viewModel.onEvent(UiEvent.DeleteItem(uiCartProduct))
        // Then
        viewModel.uiState.test {
            assertEquals(UiState.Idle, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.DisplayPurchase(listOf(uiCartProduct)), awaitItem())
        }
    }

}