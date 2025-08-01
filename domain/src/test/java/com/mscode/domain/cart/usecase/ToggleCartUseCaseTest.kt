package com.mscode.domain.cart.usecase

import com.mscode.domain.cart.model.CartProduct
import com.mscode.domain.cart.repository.CartRepository
import com.mscode.domain.common.WrapperResults
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class ToggleCartUseCaseTest {

    private val repository: CartRepository = mockk()
    private lateinit var useCase: ToggleCartUseCase

    private val cartProduct = CartProduct(
        id = 1,
        title = "Test",
        price = 9.99,
        description = "desc",
        category = "cat",
        image = "img.jpg"
    )

    @BeforeEach
    fun setUp() {
        useCase = ToggleCartUseCase(repository)
    }

    @Test
    fun `should return Success when removing product succeeds`() = runTest {
        // Given
        coEvery { repository.removeCartProduct(cartProduct) } returns 1

        // When
        val result = useCase(cartProduct, isCartProducts = true)

        // Then
        assertTrue(result is WrapperResults.Success)
    }

    @Test
    fun `should return Error when removing product fails`() = runTest {
        coEvery { repository.removeCartProduct(cartProduct) } returns 0

        val result = useCase(cartProduct, isCartProducts = true)

        assertTrue(result is WrapperResults.Error)
    }

    @Test
    fun `should return Success when adding product succeeds`() = runTest {
        coEvery { repository.addCartProduct(cartProduct) } returns 123L

        val result = useCase(cartProduct, isCartProducts = false)

        assertTrue(result is WrapperResults.Success)
    }

    @Test
    fun `should return Error when adding product fails`() = runTest {
        coEvery { repository.addCartProduct(cartProduct) } returns -1L

        val result = useCase(cartProduct, isCartProducts = false)

        assertTrue(result is WrapperResults.Error)
    }
}
