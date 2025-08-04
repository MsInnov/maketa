package com.mscode.presentation.filter.viewmodel

import app.cash.turbine.test
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.favorites.usecase.ToggleFavoriteUseCase
import com.mscode.domain.products.model.Product
import com.mscode.domain.products.usecase.GetCategoryProductsUseCase
import com.mscode.domain.products.usecase.GetProductsByCategoryUseCase
import com.mscode.domain.products.usecase.IsCartFlowUseCase
import com.mscode.domain.products.usecase.SortByPriceAscendingUseCase
import com.mscode.domain.products.usecase.SortByPriceDescendingUseCase
import com.mscode.presentation.filter.model.UiEvent
import com.mscode.presentation.filter.model.UiEvent.FilterByCategory
import com.mscode.presentation.filter.model.UiEvent.SortByPriceDescending
import com.mscode.presentation.home.mapper.ProductsUiMapper
import com.mscode.presentation.home.model.UiProduct
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FilterViewModelTest {

    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase = mockk()
    private val getCategoryProductsUseCase: GetCategoryProductsUseCase = mockk()
    private val sortByPriceAscendingUseCase: SortByPriceAscendingUseCase = mockk()
    private val sortByPriceDescendingUseCase: SortByPriceDescendingUseCase = mockk()
    private val productsUiMapper: ProductsUiMapper = mockk()
    private val isCartFlowUseCase: IsCartFlowUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()

    private lateinit var viewModel: FilterViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val product = Product.Classic(1, "Title", 10.0, "Desc", "category", "Img", false)

    private val uiProduct1 = UiProduct.Classic(1, "Title", 10.0, "Desc", "category", "Img", false, false)
    private val uiProduct2 = UiProduct.Classic(2, "Title", 10.0, "Desc", "Cat", "Img", false, false)
    private val favorite = Product.Favorite(1, "Title", 10.0, "Desc", "Cat", "Img", false)

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit UiProducts by FilteredByCategory UiState when FilterByCategory and getProductsByCategoryUseCase collected with products not empty`() = runTest {
        // Given
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { getProductsByCategoryUseCase("category") } returns flowOf(listOf(product))
        coEvery { productsUiMapper.toProductUi(product) } returns uiProduct1
        viewModel = FilterViewModel(
            getProductsByCategoryUseCase,
            getCategoryProductsUseCase,
            sortByPriceAscendingUseCase,
            sortByPriceDescendingUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            toggleFavoriteUseCase
        )
        // When
        viewModel.onEvent(FilterByCategory("category",  listOf(uiProduct1, uiProduct2)))
        // Then
        viewModel.uiState.test {
            assertEquals(com.mscode.presentation.filter.model.UiState.Idle, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(com.mscode.presentation.filter.model.UiState.FilteredByCategory(listOf(uiProduct1)), awaitItem())
        }
    }

    @Test
    fun `should emit UiProducts by FilteredByCategory UiState when isCartFlowUseCase return true And uiState is FilteredByCategory`() = runTest {
        // Given
        val isCartFlow = MutableStateFlow(listOf(1 to false))
        coEvery { isCartFlowUseCase() } returns isCartFlow
        coEvery { getProductsByCategoryUseCase("category") } returns flowOf(listOf(product))
        coEvery { productsUiMapper.toProductUi(product) } returns uiProduct1
        viewModel = FilterViewModel(
            getProductsByCategoryUseCase,
            getCategoryProductsUseCase,
            sortByPriceAscendingUseCase,
            sortByPriceDescendingUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            toggleFavoriteUseCase
        )
        // When
        viewModel.onEvent(FilterByCategory("category",  listOf(uiProduct1, uiProduct2)))
        // Then
        viewModel.uiState.test {
            assertEquals(com.mscode.presentation.filter.model.UiState.Idle, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(com.mscode.presentation.filter.model.UiState.FilteredByCategory(listOf(uiProduct1)), awaitItem())
            isCartFlow.value = listOf(1 to true)
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(com.mscode.presentation.filter.model.UiState.FilteredByCategory(listOf(uiProduct1.copy(isCart = true))), awaitItem())
        }
    }

    @Test
    fun `should emit UiProducts by FilteredByCategory UiState when SortByPriceDescending and FilterByCategory and getProductsByCategoryUseCase with products empty`() = runTest {
        // Given
        val uiProducts = listOf(uiProduct1, uiProduct2)
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { productsUiMapper.toProducts(uiProducts) } returns listOf(product)
        coEvery { sortByPriceDescendingUseCase(listOf(product)) } returns listOf(product)
        coEvery { getProductsByCategoryUseCase("category") } returns flowOf(emptyList())
        coEvery { productsUiMapper.toProductUi(product) } returns uiProduct1
        viewModel = FilterViewModel(
            getProductsByCategoryUseCase,
            getCategoryProductsUseCase,
            sortByPriceAscendingUseCase,
            sortByPriceDescendingUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            toggleFavoriteUseCase
        )
        // When
        viewModel.onEvent(SortByPriceDescending)
        viewModel.onEvent(FilterByCategory("category",  uiProducts))
        // Then
        viewModel.uiState.test {
            assertEquals(com.mscode.presentation.filter.model.UiState.Idle, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(com.mscode.presentation.filter.model.UiState.FilteredByCategory(listOf(uiProduct1)), awaitItem())
        }
    }

    @Test
    fun `should emit UiProducts by FilteredByCategory UiState when SortByPriceAscending and FilterByCategory and getProductsByCategoryUseCase with products empty`() = runTest {
        // Given
        val uiProducts = listOf(uiProduct1, uiProduct2)
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { productsUiMapper.toProducts(uiProducts) } returns listOf(product)
        coEvery { sortByPriceAscendingUseCase(listOf(product)) } returns listOf(product)
        coEvery { getProductsByCategoryUseCase("category") } returns flowOf(emptyList())
        coEvery { productsUiMapper.toProductUi(product) } returns uiProduct1
        viewModel = FilterViewModel(
            getProductsByCategoryUseCase,
            getCategoryProductsUseCase,
            sortByPriceAscendingUseCase,
            sortByPriceDescendingUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            toggleFavoriteUseCase
        )
        // When
        viewModel.onEvent(UiEvent.SortByPriceAscending)
        viewModel.onEvent(FilterByCategory("category",  uiProducts))
        // Then
        viewModel.uiState.test {
            assertEquals(com.mscode.presentation.filter.model.UiState.Idle, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(com.mscode.presentation.filter.model.UiState.FilteredByCategory(listOf(uiProduct1)), awaitItem())
        }
    }

    @Test
    fun `should emit UiProducts by FilteredByCategory UiState when SortByPriceDescending and FilterByCategory and getProductsByCategoryUseCase with products not empty`() = runTest {
        // Given
        val uiProducts = listOf(uiProduct1, uiProduct2)
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { productsUiMapper.toProducts(uiProducts) } returns listOf(product)
        coEvery { productsUiMapper.toProducts(listOf(uiProduct1)) } returns listOf(product)
        coEvery { sortByPriceDescendingUseCase(listOf(product)) } returns listOf(product)
        coEvery { getProductsByCategoryUseCase("category") } returns flowOf(listOf(product))
        coEvery { productsUiMapper.toProductUi(product) } returns uiProduct1
        viewModel = FilterViewModel(
            getProductsByCategoryUseCase,
            getCategoryProductsUseCase,
            sortByPriceAscendingUseCase,
            sortByPriceDescendingUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            toggleFavoriteUseCase
        )
        // When
        viewModel.onEvent(SortByPriceDescending)
        viewModel.onEvent(FilterByCategory("category",  uiProducts))
        // Then
        viewModel.uiState.test {
            assertEquals(com.mscode.presentation.filter.model.UiState.Idle, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(com.mscode.presentation.filter.model.UiState.FilteredByCategory(listOf(uiProduct1)), awaitItem())
        }
    }

    @Test
    fun `should emit UiProducts by FilteredByCategory UiState when SortByPriceAscending and FilterByCategory and getProductsByCategoryUseCase with products not empty`() = runTest {
        // Given
        val uiProducts = listOf(uiProduct1, uiProduct2)
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { productsUiMapper.toProducts(uiProducts) } returns listOf(product)
        coEvery { productsUiMapper.toProducts(listOf(uiProduct1)) } returns listOf(product)
        coEvery { sortByPriceAscendingUseCase(listOf(product)) } returns listOf(product)
        coEvery { getProductsByCategoryUseCase("category") } returns flowOf(listOf(product))
        coEvery { productsUiMapper.toProductUi(product) } returns uiProduct1
        viewModel = FilterViewModel(
            getProductsByCategoryUseCase,
            getCategoryProductsUseCase,
            sortByPriceAscendingUseCase,
            sortByPriceDescendingUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            toggleFavoriteUseCase
        )
        // When
        viewModel.onEvent(UiEvent.SortByPriceAscending)
        viewModel.onEvent(FilterByCategory("category",  uiProducts))
        // Then
        viewModel.uiState.test {
            assertEquals(com.mscode.presentation.filter.model.UiState.Idle, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(com.mscode.presentation.filter.model.UiState.FilteredByCategory(listOf(uiProduct1)), awaitItem())
        }
    }

    @Test
    fun `should emit ToHome when not SortByPriceAscending and not SortByPriceDescending and getProductsByCategoryUseCase with products empty`() = runTest {
        // Given
        val uiProducts = listOf(uiProduct1, uiProduct2)
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { getProductsByCategoryUseCase("category") } returns flowOf(emptyList())
        viewModel = FilterViewModel(
            getProductsByCategoryUseCase,
            getCategoryProductsUseCase,
            sortByPriceAscendingUseCase,
            sortByPriceDescendingUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            toggleFavoriteUseCase
        )
        // When
        viewModel.onEvent(FilterByCategory("category",  uiProducts))
        // Then
        viewModel.uiState.test {
            assertEquals(com.mscode.presentation.filter.model.UiState.Idle, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(com.mscode.presentation.filter.model.UiState.ToHome, awaitItem())
        }
    }

    @Test
    fun `should emit ToHome and Idle when not SortByPriceAscending and not SortByPriceDescending and getProductsByCategoryUseCase with products empty and Idle is called`() = runTest {
        // Given
        val uiProducts = listOf(uiProduct1, uiProduct2)
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { getProductsByCategoryUseCase("category") } returns flowOf(emptyList())
        viewModel = FilterViewModel(
            getProductsByCategoryUseCase,
            getCategoryProductsUseCase,
            sortByPriceAscendingUseCase,
            sortByPriceDescendingUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            toggleFavoriteUseCase
        )
        // When
        viewModel.onEvent(FilterByCategory("category",  uiProducts))
        // Then
        viewModel.uiState.test {
            assertEquals(com.mscode.presentation.filter.model.UiState.Idle, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(com.mscode.presentation.filter.model.UiState.ToHome, awaitItem())
            viewModel.onEvent(UiEvent.Idle)
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(com.mscode.presentation.filter.model.UiState.Idle, awaitItem())
        }
    }

    @Test
    fun `should emit CategoryProducts with products when GetCategory is called`() = runTest {
        // Given
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { getCategoryProductsUseCase() } returns listOf("category")
        viewModel = FilterViewModel(
            getProductsByCategoryUseCase,
            getCategoryProductsUseCase,
            sortByPriceAscendingUseCase,
            sortByPriceDescendingUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            toggleFavoriteUseCase
        )
        // When
        viewModel.onEvent(UiEvent.GetCategory)
        // Then
        viewModel.uiState.test {
            assertEquals(com.mscode.presentation.filter.model.UiState.Idle, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(com.mscode.presentation.filter.model.UiState.CategoryProducts(listOf("category")), awaitItem())
        }
    }

    @Test
    fun `should emit FilteredByCategory with products when UpdateFavorite is called`() = runTest {
        // Given
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { productsUiMapper.toFavorite(uiProduct1) } returns favorite
        coEvery { toggleFavoriteUseCase(favorite, false) } returns WrapperResults.Success(Unit)
        coEvery { getProductsByCategoryUseCase("category") } returns flowOf(listOf(product))
        coEvery { productsUiMapper.toProductUi(product) } returns uiProduct1
        viewModel = FilterViewModel(
            getProductsByCategoryUseCase,
            getCategoryProductsUseCase,
            sortByPriceAscendingUseCase,
            sortByPriceDescendingUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            toggleFavoriteUseCase
        )
        // When
        viewModel.onEvent(FilterByCategory("category",  listOf(uiProduct1, uiProduct2)))
        // Then
        viewModel.uiState.test {
            assertEquals(com.mscode.presentation.filter.model.UiState.Idle, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(com.mscode.presentation.filter.model.UiState.FilteredByCategory(listOf(uiProduct1)), awaitItem())
            viewModel.onEvent(UiEvent.UpdateFavorite(uiProduct1))
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(com.mscode.presentation.filter.model.UiState.FilteredByCategory(listOf(uiProduct1.copy(isFavorite = true))), awaitItem())
        }
    }
}