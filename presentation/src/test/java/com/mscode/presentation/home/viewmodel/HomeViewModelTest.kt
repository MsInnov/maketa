package com.mscode.presentation.home.viewmodel

import app.cash.turbine.test
import com.mscode.domain.common.WrapperResults.*
import com.mscode.domain.favorites.model.FavoriteProducts
import com.mscode.domain.favorites.usecase.GetAllFavoritesFlowUseCase
import com.mscode.domain.favorites.usecase.GetAllFavoritesUseCase
import com.mscode.domain.favorites.usecase.SaveFavoriteIsDisplayedUseCase
import com.mscode.domain.favorites.usecase.ToggleFavoriteUseCase
import com.mscode.domain.login.usecase.GetTokenUseCase
import com.mscode.domain.products.model.Product
import com.mscode.domain.products.usecase.GetProductsUseCase
import com.mscode.presentation.home.mapper.ProductsUiMapper
import com.mscode.presentation.home.model.UiEvent
import com.mscode.presentation.home.model.UiProducts
import com.mscode.presentation.home.model.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val getProductsUseCase: GetProductsUseCase = mockk()
    private val productsUiMapper: ProductsUiMapper = mockk()
    private val getTokenUseCase: GetTokenUseCase = mockk()
    private val getAllFavoritesUseCase: GetAllFavoritesUseCase = mockk()
    private val getAllFavoritesFlowUseCase: GetAllFavoritesFlowUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()
    private val saveFavoriteIsDisplayed: SaveFavoriteIsDisplayedUseCase = mockk()
    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val product = Product(1, "Title", 10.0, "Desc", "Cat", "Img", false)
    private val favoriteProducts = FavoriteProducts(1, "Title", 10.0, "Desc", "Cat", "Img", false)
    private val uiProducts = UiProducts(1, "Title", 10.0, "Desc", "Cat", "Img", false, false)
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit Products UiState when getProductsUseCase returns Success`() = runTest {
        // Given
        val domainProducts = listOf(product)
        val uiProducts = listOf(uiProducts)

        coEvery { getProductsUseCase() } returns Success(domainProducts)
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        every { productsUiMapper.toProductsUi(product) } returns uiProducts.first()
        //When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            saveFavoriteIsDisplayed
        )
        //Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(uiProducts), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Error UiState when getProductsUseCase returns Error`() = runTest {
        // Given
        coEvery { getProductsUseCase() } returns Error(Exception("Something went wrong"))
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        //When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            saveFavoriteIsDisplayed
        )
        //Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit true uiStateFavoriteEnabled when getAllFavoritesFlowUseCase returns favorites`() = runTest {
        // Given
        val domainProducts = listOf(product)
        val uiProducts = listOf(uiProducts)

        coEvery { getProductsUseCase() } returns Success(domainProducts)
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf(
            listOf(favoriteProducts)
        )
        every { productsUiMapper.toProductsUi(product) } returns uiProducts.first()
        //When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            saveFavoriteIsDisplayed
        )
        //Then
        viewModel.uiStateFavoriteEnabled.test {
            assertEquals(false, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit false uiStateFavoriteEnabled when getAllFavoritesFlowUseCase returns favorites empty`() = runTest {
        // Given
        val domainProducts = listOf(product)
        val uiProducts = listOf(uiProducts)
        coEvery { getProductsUseCase() } returns Success(domainProducts)
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf(
            emptyList()
        )
        every { productsUiMapper.toProductsUi(product) } returns uiProducts.first()
        //When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            saveFavoriteIsDisplayed
        )
        //Then
        viewModel.uiStateFavoriteEnabled.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit true uiStateFavoriteDisplay when DisplayFavorites and token not null`() = runTest {
        // Given
        coEvery { getProductsUseCase() } returns Success(emptyList())
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        coEvery { getTokenUseCase() } returns "1234"
        //When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            saveFavoriteIsDisplayed
        )
        viewModel.onEvent(UiEvent.DisplayFavorites)
        //Then
        viewModel.uiStateFavoriteDisplay.test {
            assertEquals(false, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit false uiStateFavoriteDisplay when DisplayFavorites and token null`() = runTest {
        // Given
        coEvery { getProductsUseCase() } returns Success(emptyList())
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        coEvery { getTokenUseCase() } returns null
        //When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            saveFavoriteIsDisplayed
        )
        viewModel.onEvent(UiEvent.DisplayFavorites)
        //Then
        viewModel.uiStateFavoriteDisplay.test {
            assertEquals(false, awaitItem()) // initial
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Products with new products when UpdateFavorite and toggleFavoriteUseCase Success`() = runTest {
        // Given
        coEvery { getProductsUseCase() } returns Success(listOf(product))
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        every { productsUiMapper.toFavoriteProducts(uiProducts.copy(title = "titre"))} returns favoriteProducts
        coEvery { toggleFavoriteUseCase(favoriteProducts, false) } returns Success(Unit)
        every { productsUiMapper.toProductsUi(product) } returns uiProducts
        //When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            saveFavoriteIsDisplayed
        )
        viewModel.onEvent(UiEvent.UpdateFavorite(uiProducts.copy(title = "titre")))
        //Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProducts)), awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProducts.copy(isFavorite = true))), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Products with favoriteProducts when LoadProductsFavorites and showFavorites true`() = runTest {
        // Given
        coEvery { getProductsUseCase() } returns Success(listOf(product))
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        every { saveFavoriteIsDisplayed(true) } returns Unit
        every { productsUiMapper.toProductsUi(product) } returns uiProducts.copy(title = "titre")
        every { productsUiMapper.toProductsUi(favoriteProducts) } returns uiProducts.copy(isFavorite = true)
        coEvery { getAllFavoritesUseCase() } returns listOf(favoriteProducts)
        //When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            saveFavoriteIsDisplayed
        )
        viewModel.onEvent(UiEvent.LoadProductsFavorites)
        //Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProducts.copy(title = "titre"))), awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProducts.copy(isFavorite = true))), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { saveFavoriteIsDisplayed(true) }
    }

    @Test
    fun `should emit Products with new products when LoadProductsFavorites and showFavorites false`() = runTest {
        // Given
        coEvery { getProductsUseCase() } returns Success(listOf(product)) andThen Success(listOf(product.copy(isFavorite = false, title = "titre")))
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        coEvery { getAllFavoritesUseCase() } returns listOf(favoriteProducts)
        every { saveFavoriteIsDisplayed(true) } returns Unit
        every { saveFavoriteIsDisplayed(false) } returns Unit
        every { productsUiMapper.toProductsUi(product) } returns uiProducts
        every { productsUiMapper.toProductsUi(product.copy(isFavorite = false, title = "titre")) } returns uiProducts.copy(title = "titre")
        every { productsUiMapper.toProductsUi(favoriteProducts) } returns uiProducts.copy(isFavorite = true)
        coEvery { getAllFavoritesUseCase() } returns listOf(favoriteProducts)
        //When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            saveFavoriteIsDisplayed
        )
        viewModel.onEvent(UiEvent.LoadProductsFavorites)
        viewModel.onEvent(UiEvent.LoadProductsFavorites)
        //Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProducts)), awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProducts.copy(isFavorite = true))), awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProducts.copy(title = "titre"))), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { saveFavoriteIsDisplayed(true) }
    }

    @Test
    fun `should emit Products with new products when LoadProduct and getProductsUseCase Success`() = runTest {
        // Given
        coEvery { getProductsUseCase() } returns Success(listOf(product)) andThen Success(listOf(product.copy(title = "titre")))
        every { saveFavoriteIsDisplayed(true) } returns Unit
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        every { productsUiMapper.toProductsUi(product) } returns uiProducts
        every { productsUiMapper.toProductsUi(product.copy(title = "titre")) } returns uiProducts.copy(title = "titre")
        //When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            saveFavoriteIsDisplayed
        )
        viewModel.onEvent(UiEvent.LoadProduct)
        //Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProducts)), awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProducts.copy(title = "titre"))), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit false to uiStateFavorite and saveFavoriteIsDisplayed false when LoadProduct`() = runTest {
        // Given
        coEvery { getProductsUseCase() } returns Success(listOf(product)) andThen Success(listOf(product.copy(title = "titre")))
        every { saveFavoriteIsDisplayed(true) } returns Unit
        every { saveFavoriteIsDisplayed(false) } returns Unit
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        coEvery { getAllFavoritesUseCase() } returns emptyList()
        every { productsUiMapper.toProductsUi(product) } returns uiProducts
        every { productsUiMapper.toProductsUi(product.copy(title = "titre")) } returns uiProducts.copy(title = "titre")
        //When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            saveFavoriteIsDisplayed
        )
        viewModel.onEvent(UiEvent.LoadProductsFavorites)
        viewModel.onEvent(UiEvent.LoadProduct)
        //Then
        viewModel.uiStateFavorite.test {
            assertEquals(false, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { saveFavoriteIsDisplayed(false) }
    }
}
