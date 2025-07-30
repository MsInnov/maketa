package com.mscode.presentation.home.viewmodel

import app.cash.turbine.test
import com.mscode.domain.common.WrapperResults.*
import com.mscode.domain.products.model.Product
import com.mscode.domain.products.usecase.GetProductsUseCase
import com.mscode.presentation.home.mapper.ProductsUiMapper
import com.mscode.presentation.home.model.UiProducts
import com.mscode.presentation.home.model.UiState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val getProductsUseCase: GetProductsUseCase = mockk()
    private val productsUiMapper: ProductsUiMapper = mockk()
    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit Products UiState when use case returns Success`() = runTest {
        // Given
        val domainProducts = listOf(
            Product(1, "Title", 10.0, "Desc", "Cat", "Img", false)
        )
        val uiProducts = listOf(
            UiProducts(1, "Title", 10.0, "Desc", "Cat", "Img", false, false)
        )

        coEvery { getProductsUseCase() } returns Success(domainProducts)
        every { productsUiMapper.toProductsUi(any()) } returns uiProducts.first()

        viewModel = HomeViewModel(getProductsUseCase, productsUiMapper)

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(uiProducts), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Error UiState when use case returns Error`() = runTest {
        coEvery { getProductsUseCase() } returns Error(Exception("Something went wrong"))
        viewModel = HomeViewModel(getProductsUseCase, productsUiMapper)

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
