package com.mscode.domain.cart.usecase

import com.mscode.domain.cart.repository.CartRepository
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.products.model.Product
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class GetCartUseCaseTest {

    private val repository: CartRepository = mockk()
    private lateinit var useCase: GetCartUseCase

    private val cart = Product.Cart(
        id = 1,
        title = "Title",
        price = 9.99,
        description = "Desc",
        category = "Category",
        image = "image.jpg"
    )

    @BeforeEach
    fun setUp() {
        useCase = GetCartUseCase(repository)
    }

    @Test
    fun `should return cart list when repository returns success`() = runTest {
        // Given
        val expected = WrapperResults.Success(listOf(cart))
        coEvery { repository.getCart() } returns expected

        // When
        val result = useCase()

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `should return error when repository returns error`() = runTest {
        // Given
        val expected = WrapperResults.Error(Exception("Network error"))
        coEvery { repository.getCart() } returns expected

        // When
        val result = useCase()

        // Then
        assertEquals(expected, result)
    }
}
