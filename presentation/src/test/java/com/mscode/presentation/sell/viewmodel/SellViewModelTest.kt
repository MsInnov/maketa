package com.mscode.presentation.sell.viewmodel

import app.cash.turbine.test
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.products.model.Product
import com.mscode.domain.products.usecase.SellProductUseCase
import com.mscode.presentation.sell.mapper.SellProductUiMapper
import com.mscode.presentation.sell.model.SellProductUi
import com.mscode.presentation.sell.model.UiEvent
import com.mscode.presentation.sell.model.UiState
import com.mscode.presentation.sell.model.UiState.Idle
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SellViewModelTest {

    private val sellProductUseCase: SellProductUseCase = mockk()
    private val sellProductUiMapper: SellProductUiMapper = mockk()

    private lateinit var viewModel: SellViewModel

    private val products = Product(1, "Title", 10.0, "Desc", "Cat", "Img", false)
    private val sellProductUi = SellProductUi("Title", 10.0, "Desc", "Cat", "Img")
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SellViewModel(sellProductUseCase, sellProductUiMapper)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEvent SellingProduct with Success result should emit Success state`() = runTest {
        coEvery { sellProductUiMapper.toProducts(sellProductUi) } returns products
        coEvery { sellProductUseCase(products) } returns WrapperResults.Success(Unit)

        viewModel.uiState.test {
            assertEquals(Idle, awaitItem())
            viewModel.onEvent(UiEvent.SellingProduct(sellProductUi))
            assertEquals(UiState.Success, awaitItem())
        }
    }

    @Test
    fun `onEvent SellingProduct with Failure result should emit Failure state`() = runTest {
        coEvery { sellProductUiMapper.toProducts(sellProductUi) } returns products
        coEvery { sellProductUseCase(products) } returns WrapperResults.Error(Exception("error"))

        viewModel.uiState.test {
            assertEquals(Idle, awaitItem())
            viewModel.onEvent(UiEvent.SellingProduct(sellProductUi))
            assertEquals(UiState.Failure, awaitItem())
        }
    }

    @Test
    fun `onEvent Idle should reset state to Idle`() = runTest {
        coEvery { sellProductUiMapper.toProducts(sellProductUi) } returns products
        coEvery { sellProductUseCase(products) } returns WrapperResults.Success(Unit)
        viewModel.uiState.test {
            assertEquals(Idle, awaitItem())
            viewModel.onEvent(UiEvent.SellingProduct(sellProductUi))
            assertEquals(UiState.Success, awaitItem())
            viewModel.onEvent(UiEvent.Idle)
            assertEquals(Idle, awaitItem())
        }
    }
}
