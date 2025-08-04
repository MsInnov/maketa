package com.mscode.domain.favorites.usecase

import com.mscode.domain.favorites.repository.FavoriteRepository
import com.mscode.domain.products.model.Product
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetAllFavoritesFlowUseCaseTest {

    private lateinit var favoritesRepository: FavoriteRepository
    private lateinit var getAllFavoritesFlowUseCase: GetAllFavoritesFlowUseCase

    private val favorite = Product.Favorite(1, "Title", 10.0, "Desc", "Cat", "Img", false)

    @BeforeEach
    fun setUp() {
        favoritesRepository = mockk()
        getAllFavoritesFlowUseCase = GetAllFavoritesFlowUseCase(favoritesRepository)
    }

    @Test
    fun `should return favorites flow from repository`() = runTest {
        // Given
        val favorites = listOf(favorite, favorite.copy(id = 2))
        every { favoritesRepository.getFavoritesFlow() } returns flowOf(favorites)

        // When
        val result = getAllFavoritesFlowUseCase().first()

        // Then
        assertEquals(favorites, result)
    }
}
