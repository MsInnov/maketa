package com.mscode.domain.favorites.usecase

import com.mscode.domain.favorites.model.FavoriteProducts
import com.mscode.domain.favorites.repository.FavoriteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetAllFavoritesUseCaseTest {

    private lateinit var favoritesRepository: FavoriteRepository
    private lateinit var getAllFavoritesUseCase: GetAllFavoritesUseCase

    private val favoriteProducts = FavoriteProducts(1, "Title", 10.0, "Desc", "Cat", "Img", false)

    @BeforeEach
    fun setUp() {
        favoritesRepository = mockk()
        getAllFavoritesUseCase = GetAllFavoritesUseCase(favoritesRepository)
    }

    @Test
    fun `should return list of favorites from repository`() = runTest {
        // Given
        val expectedFavorites = listOf(
            favoriteProducts,
            favoriteProducts.copy(id = 2)
        )
        coEvery { favoritesRepository.getFavorites() } returns expectedFavorites

        // When
        val result = getAllFavoritesUseCase()

        // Then
        assertEquals(expectedFavorites, result)
        coVerify(exactly = 1) { favoritesRepository.getFavorites() }
    }
}
