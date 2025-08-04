package com.mscode.domain.favorites.usecase

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.favorites.repository.FavoriteRepository
import com.mscode.domain.products.model.Product
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class ToggleFavoriteUseCaseTest {

    private lateinit var favoritesRepository: FavoriteRepository
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    private val favorite = Product.Favorite(1, "Title", 10.0, "Desc", "Cat", "Img", false)


    @BeforeEach
    fun setUp() {
        favoritesRepository = mockk()
        toggleFavoriteUseCase = ToggleFavoriteUseCase(favoritesRepository)
    }

    @Test
    fun `should return Success when removing favorite succeeds`() = runTest {
        // Given
        coEvery { favoritesRepository.deleteFavorite(favorite) } returns 1

        // When
        val result = toggleFavoriteUseCase(favorite, isFavorite = true)

        // Then
        assertTrue(result is WrapperResults.Success)
        coVerify(exactly = 1) { favoritesRepository.deleteFavorite(favorite) }
    }

    @Test
    fun `should return Error when removing favorite fails`() = runTest {
        // Given
        coEvery { favoritesRepository.deleteFavorite(favorite) } returns 0

        // When
        val result = toggleFavoriteUseCase(favorite, isFavorite = true)

        // Then
        assertTrue(result is WrapperResults.Error)
        coVerify(exactly = 1) { favoritesRepository.deleteFavorite(favorite) }
    }

    @Test
    fun `should return Success when adding favorite succeeds`() = runTest {
        // Given
        coEvery { favoritesRepository.addFavorite(favorite) } returns 5L

        // When
        val result = toggleFavoriteUseCase(favorite, isFavorite = false)

        // Then
        assertTrue(result is WrapperResults.Success)
        coVerify(exactly = 1) { favoritesRepository.addFavorite(favorite) }
    }

    @Test
    fun `should return Error when adding favorite fails`() = runTest {
        // Given
        coEvery { favoritesRepository.addFavorite(favorite) } returns -1L

        // When
        val result = toggleFavoriteUseCase(favorite, isFavorite = false)

        // Then
        assertTrue(result is WrapperResults.Error)
        coVerify(exactly = 1) { favoritesRepository.addFavorite(favorite) }
    }
}
