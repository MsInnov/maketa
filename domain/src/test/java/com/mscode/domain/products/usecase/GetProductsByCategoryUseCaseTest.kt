package com.mscode.domain.products.usecase

import com.mscode.domain.favorites.model.FavoriteProduct
import com.mscode.domain.favorites.repository.FavoriteRepository
import com.mscode.domain.products.model.Product
import com.mscode.domain.products.repository.ProductsRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class GetProductsByCategoryUseCaseTest {

    private val productsRepository: ProductsRepository = mockk()
    private val favoritesRepository: FavoriteRepository = mockk()

    private lateinit var useCase: GetProductsByCategoryUseCase

    private val product = Product(
        id = 1,
        title = "Test Product",
        price = 19.99,
        description = "A test product",
        category = "test",
        image = "http://example.com/image.png",
        isFavorite = false
    )

    @BeforeEach
    fun setup() {
        useCase = GetProductsByCategoryUseCase(productsRepository, favoritesRepository)
    }

    @Test
    fun `invoke with NONE category returns empty list`() = runTest {
        // When
        val flow = useCase("NONE")

        // Then
        assertEquals(emptyList<Product>(), flow.first())
    }

    @Test
    fun `invoke with non-NONE category and favorites displayed returns products flow`() = runTest {
        val category = "Electronics"

        coEvery { favoritesRepository.getIsDisplayed() } returns true
        every { favoritesRepository.getFavoritesFilteredByCategoryFlow(category) } returns flowOf(listOf(product))

        val flow = useCase(category)

        assertEquals(listOf(product), flow.first())
    }

    @Test
    fun `invoke with non-NONE category and favorites not displayed returns products list as flow`() =
        runTest {
            val category = "Books"

            coEvery { favoritesRepository.getIsDisplayed() } returns false
            coEvery { productsRepository.getProductsFilteredByCategory(category) } returns listOf(product)

            val flow = useCase(category)

            assertEquals(listOf(product), flow.first())
        }
}
