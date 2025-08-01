package com.mscode.presentation.home.viewmodel

import app.cash.turbine.test
import com.mscode.domain.cart.model.CartProduct
import com.mscode.domain.cart.usecase.ToggleCartUseCase
import com.mscode.domain.common.WrapperResults.*
import com.mscode.domain.favorites.model.FavoriteProduct
import com.mscode.domain.favorites.usecase.GetAllFavoritesFlowUseCase
import com.mscode.domain.favorites.usecase.GetAllFavoritesUseCase
import com.mscode.domain.favorites.usecase.SaveFavoriteIsDisplayedUseCase
import com.mscode.domain.favorites.usecase.ToggleFavoriteUseCase
import com.mscode.domain.login.usecase.GetTokenUseCase
import com.mscode.domain.products.model.Product
import com.mscode.domain.products.usecase.GetProductsUseCase
import com.mscode.domain.products.usecase.IsCartFlowUseCase
import com.mscode.presentation.home.mapper.ProductsUiMapper
import com.mscode.presentation.home.model.UiEvent
import com.mscode.presentation.home.model.UiProduct
import com.mscode.presentation.home.model.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val toggleCartUseCase: ToggleCartUseCase = mockk()
    private val isCartFlowUseCase: IsCartFlowUseCase = mockk()
    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val product = Product(1, "Title", 10.0, "Desc", "Cat", "Img", false)
    private val favoriteProducts = FavoriteProduct(1, "Title", 10.0, "Desc", "Cat", "Img", false)
    private val uiProduct = UiProduct(1, "Title", 10.0, "Desc", "Cat", "Img", false, false)
    private val cartProduct = CartProduct(1, "Title", 10.0, "Desc", "Cat", "Img")

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
        val uiProducts = listOf(uiProduct)

        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { getProductsUseCase() } returns Success(domainProducts)
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        every { productsUiMapper.toProductUi(product) } returns uiProducts.first()
        // When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleCartUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            saveFavoriteIsDisplayed
        )
        // Then
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
        // When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleCartUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            saveFavoriteIsDisplayed
        )
        // Then
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
        val uiProducts = listOf(uiProduct)

        coEvery { getProductsUseCase() } returns Success(domainProducts)
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf(
            listOf(favoriteProducts)
        )
        every { productsUiMapper.toProductUi(product) } returns uiProducts.first()
        // When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleCartUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            saveFavoriteIsDisplayed
        )
        // Then
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
        val uiProducts = listOf(uiProduct)
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { getProductsUseCase() } returns Success(domainProducts)
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf(
            emptyList()
        )
        every { productsUiMapper.toProductUi(product) } returns uiProducts.first()
        // When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleCartUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            saveFavoriteIsDisplayed
        )
        // Then
        viewModel.uiStateFavoriteEnabled.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit true uiStateFavoriteDisplay when DisplayFavorites and token not null`() = runTest {
        // Given
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { getProductsUseCase() } returns Success(emptyList())
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        coEvery { getTokenUseCase() } returns "1234"
        // When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleCartUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            saveFavoriteIsDisplayed
        )
        viewModel.onEvent(UiEvent.DisplayFavoritesAndCart)
        // Then
        viewModel.uiStateFavoriteAndCartDisplay.test {
            assertEquals(false, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit false uiStateFavoriteDisplay when DisplayFavorites and token null`() = runTest {
        // Given
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { getProductsUseCase() } returns Success(emptyList())
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        coEvery { getTokenUseCase() } returns null
        // When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleCartUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            saveFavoriteIsDisplayed
        )
        viewModel.onEvent(UiEvent.DisplayFavoritesAndCart)
        // Then
        viewModel.uiStateFavoriteAndCartDisplay.test {
            assertEquals(false, awaitItem()) // initial
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Products with new products when UpdateFavorite and toggleFavoriteUseCase Success`() = runTest {
        // Given
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { getProductsUseCase() } returns Success(listOf(product))
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        every { productsUiMapper.toFavoriteProduct(uiProduct.copy(title = "titre"))} returns favoriteProducts
        coEvery { toggleFavoriteUseCase(favoriteProducts, false) } returns Success(Unit)
        every { productsUiMapper.toProductUi(product) } returns uiProduct
        // When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleCartUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            saveFavoriteIsDisplayed
        )
        viewModel.onEvent(UiEvent.UpdateFavorite(uiProduct.copy(title = "titre")))
        // Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProduct)), awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProduct.copy(isFavorite = true))), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Products with favoriteProducts when LoadProductsFavorites and showFavorites true`() = runTest {
        // Given
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { getProductsUseCase() } returns Success(listOf(product))
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        every { saveFavoriteIsDisplayed(true) } returns Unit
        every { productsUiMapper.toProductUi(product) } returns uiProduct.copy(title = "titre")
        every { productsUiMapper.toProductUi(favoriteProducts) } returns uiProduct.copy(isFavorite = true)
        coEvery { getAllFavoritesUseCase() } returns listOf(favoriteProducts)
        // When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleCartUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            saveFavoriteIsDisplayed
        )
        viewModel.onEvent(UiEvent.LoadProductsFavorites)
        // Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProduct.copy(title = "titre"))), awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProduct.copy(isFavorite = true))), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { saveFavoriteIsDisplayed(true) }
    }

    @Test
    fun `should emit Products with new products when LoadProductsFavorites and showFavorites false`() = runTest {
        // Given
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { getProductsUseCase() } returns Success(listOf(product)) andThen Success(listOf(product.copy(isFavorite = false, title = "titre")))
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        coEvery { getAllFavoritesUseCase() } returns listOf(favoriteProducts)
        every { saveFavoriteIsDisplayed(true) } returns Unit
        every { saveFavoriteIsDisplayed(false) } returns Unit
        every { productsUiMapper.toProductUi(product) } returns uiProduct
        every { productsUiMapper.toProductUi(product.copy(isFavorite = false, title = "titre")) } returns uiProduct.copy(title = "titre")
        every { productsUiMapper.toProductUi(favoriteProducts) } returns uiProduct.copy(isFavorite = true)
        coEvery { getAllFavoritesUseCase() } returns listOf(favoriteProducts)
        // When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleCartUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            saveFavoriteIsDisplayed
        )
        viewModel.onEvent(UiEvent.LoadProductsFavorites)
        viewModel.onEvent(UiEvent.LoadProductsFavorites)
        // Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProduct)), awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProduct.copy(isFavorite = true))), awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProduct.copy(title = "titre"))), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { saveFavoriteIsDisplayed(true) }
    }

    @Test
    fun `should emit Products with new products when LoadProduct and getProductsUseCase Success`() = runTest {
        // Given
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { getProductsUseCase() } returns Success(listOf(product)) andThen Success(listOf(product.copy(title = "titre")))
        every { saveFavoriteIsDisplayed(true) } returns Unit
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        every { productsUiMapper.toProductUi(product) } returns uiProduct
        every { productsUiMapper.toProductUi(product.copy(title = "titre")) } returns uiProduct.copy(title = "titre")
        // When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleCartUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            saveFavoriteIsDisplayed
        )
        viewModel.onEvent(UiEvent.LoadProduct)
        // Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProduct)), awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProduct.copy(title = "titre"))), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit false to uiStateFavorite and saveFavoriteIsDisplayed false when LoadProduct`() = runTest {
        // Given
        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { getProductsUseCase() } returns Success(listOf(product)) andThen Success(listOf(product.copy(title = "titre")))
        every { saveFavoriteIsDisplayed(true) } returns Unit
        every { saveFavoriteIsDisplayed(false) } returns Unit
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        coEvery { getAllFavoritesUseCase() } returns emptyList()
        every { productsUiMapper.toProductUi(product) } returns uiProduct
        every { productsUiMapper.toProductUi(product.copy(title = "titre")) } returns uiProduct.copy(title = "titre")
        // When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleCartUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            saveFavoriteIsDisplayed
        )
        viewModel.onEvent(UiEvent.LoadProductsFavorites)
        viewModel.onEvent(UiEvent.LoadProduct)
        // Then
        viewModel.uiStateFavorite.test {
            assertEquals(false, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { saveFavoriteIsDisplayed(false) }
    }

    @Test
    fun `should emit false and true to isCart of product when isCartFlowUseCase collected`() = runTest {
        // Given
        val domainProducts = listOf(product)
        val uiProducts = listOf(uiProduct)
        val isCartFlow = MutableStateFlow(listOf(1 to false))

        coEvery { isCartFlowUseCase() } returns isCartFlow
        coEvery { getProductsUseCase() } returns Success(domainProducts)
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        every { productsUiMapper.toProductUi(product) } returns uiProducts.first()
        // When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleCartUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            saveFavoriteIsDisplayed
        )
        // Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(uiProducts), awaitItem())
            isCartFlow.value = listOf(1 to true)
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProduct.copy(isCart = true))), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit false and true to isCart of product when onEvent is UpdateCart and toggleFavoriteUseCase return Success`() = runTest {
        // Given
        val domainProducts = listOf(product)
        val uiProducts = listOf(uiProduct)

        coEvery { isCartFlowUseCase() } returns flowOf(listOf(1 to false))
        coEvery { getProductsUseCase() } returns Success(domainProducts)
        coEvery { getAllFavoritesFlowUseCase() } returns flowOf()
        every { productsUiMapper.toProductUi(product) } returns uiProducts.first()
        every { productsUiMapper.toCartProduct(uiProduct) } returns cartProduct
        coEvery { toggleCartUseCase(cartProduct, false) } returns Success(Unit)
        // When
        viewModel = HomeViewModel(
            getTokenUseCase,
            getAllFavoritesUseCase,
            getAllFavoritesFlowUseCase,
            toggleCartUseCase,
            toggleFavoriteUseCase,
            getProductsUseCase,
            productsUiMapper,
            isCartFlowUseCase,
            saveFavoriteIsDisplayed
        )
        viewModel.onEvent(UiEvent.UpdateCart(uiProduct))
        // Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem()) // initial
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(uiProducts), awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Products(listOf(uiProduct.copy(isCart = true))), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
