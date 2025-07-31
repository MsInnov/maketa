package com.mscode.domain.favorites.usecase

import com.mscode.domain.favorites.repository.FavoriteRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SaveFavoriteIsDisplayedUseCaseTest {

    private lateinit var favoritesRepository: FavoriteRepository
    private lateinit var saveFavoriteIsDisplayedUseCase: SaveFavoriteIsDisplayedUseCase

    @BeforeEach
    fun setUp() {
        favoritesRepository = mockk(relaxed = true)
        saveFavoriteIsDisplayedUseCase = SaveFavoriteIsDisplayedUseCase(favoritesRepository)
    }

    @Test
    fun `should call repository with true`() {
        // When
        saveFavoriteIsDisplayedUseCase(true)

        // Then
        verify(exactly = 1) { favoritesRepository.saveFavoriteIsDisplayed(true) }
    }

    @Test
    fun `should call repository with false`() {
        // When
        saveFavoriteIsDisplayedUseCase(false)

        // Then
        verify(exactly = 1) { favoritesRepository.saveFavoriteIsDisplayed(false) }
    }
}
