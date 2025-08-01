package com.mscode.domain.favorites.usecase

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.favorites.model.FavoriteProduct
import com.mscode.domain.favorites.repository.FavoriteRepository
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

    private val favorite = FavoriteProduct(1, "Title", 10.0, "Desc", "Cat", "Img", false)


    @BeforeEach
    fun setUp() {
        favoritesRepository = mockk()
        toggleFavoriteUseCase = ToggleFavoriteUseCase(favoritesRepository)
    }

    @Test
    fun `should return Success when removing favorite succeeds`() = runTest {
        // Given
        coEvery { favoritesRepository.deleteFavorites(favorite) } returns 1

        // When
        val result = toggleFavoriteUseCase(favorite, isFavoriteProducts = true)

        // Then
        assertTrue(result is WrapperResults.Success)
        coVerify(exactly = 1) { favoritesRepository.deleteFavorites(favorite) }
    }

    @Test
    fun `should return Error when removing favorite fails`() = runTest {
        // Given
        coEvery { favoritesRepository.deleteFavorites(favorite) } returns 0

        // When
        val result = toggleFavoriteUseCase(favorite, isFavoriteProducts = true)

        // Then
        assertTrue(result is WrapperResults.Error)
        coVerify(exactly = 1) { favoritesRepository.deleteFavorites(favorite) }
    }

    @Test
    fun `should return Success when adding favorite succeeds`() = runTest {
        // Given
        coEvery { favoritesRepository.addFavorites(favorite) } returns 5L

        // When
        val result = toggleFavoriteUseCase(favorite, isFavoriteProducts = false)

        // Then
        assertTrue(result is WrapperResults.Success)
        coVerify(exactly = 1) { favoritesRepository.addFavorites(favorite) }
    }

    @Test
    fun `should return Error when adding favorite fails`() = runTest {
        // Given
        coEvery { favoritesRepository.addFavorites(favorite) } returns -1L

        // When
        val result = toggleFavoriteUseCase(favorite, isFavoriteProducts = false)

        // Then
        assertTrue(result is WrapperResults.Error)
        coVerify(exactly = 1) { favoritesRepository.addFavorites(favorite) }
    }
}
