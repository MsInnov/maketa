package com.mscode.domain.cart.usecase

import com.mscode.domain.cart.repository.CartRepository
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.products.model.Product
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class RemoveCartProductUseCaseTest {

    private val repository: CartRepository = mockk()
    private lateinit var useCase: RemoveCartProductUseCase

    private val cart = Product.Cart(
        id = 1,
        title = "Title",
        price = 9.99,
        description = "Desc",
        category = "Cat",
        image = "img.jpg"
    )

    @BeforeEach
    fun setup() {
        useCase = RemoveCartProductUseCase(repository)
    }

    @Test
    fun `should return Success when repository returns non-zero`() = runTest {
        // Given
        coEvery { repository.removeCartProduct(cart) } returns 1

        // When
        val result = useCase(cart)

        // Then
        assertTrue(result is WrapperResults.Success)
    }

    @Test
    fun `should return Error when repository returns zero`() = runTest {
        // Given
        coEvery { repository.removeCartProduct(cart) } returns 0

        // When
        val result = useCase(cart)

        // Then
        assertTrue(result is WrapperResults.Error)
    }
}
