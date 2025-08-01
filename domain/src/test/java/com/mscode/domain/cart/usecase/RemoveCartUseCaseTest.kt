package com.mscode.domain.cart.usecase

import com.mscode.domain.cart.repository.CartRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class RemoveCartUseCaseTest {

    private val repository: CartRepository = mockk()
    private lateinit var useCase: RemoveCartUseCase

    @BeforeEach
    fun setup() {
        useCase = RemoveCartUseCase(repository)
    }

    @Test
    fun `should call repository removeCart`() = runTest {
        // Given
        coEvery { repository.removeCart() } just Runs

        // When
        useCase()

        // Then
        coVerify(exactly = 1) { repository.removeCart() }
    }
}