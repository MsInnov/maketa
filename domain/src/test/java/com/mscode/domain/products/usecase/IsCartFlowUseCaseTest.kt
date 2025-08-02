package com.mscode.domain.products.usecase

import com.mscode.domain.products.repository.ProductsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class IsCartFlowUseCaseTest {

    private val repository: ProductsRepository = mockk()
    private lateinit var useCase: IsCartFlowUseCase

    @BeforeEach
    fun setup() {
        useCase = IsCartFlowUseCase(repository)
    }

    @Test
    fun `should emit true when repository emits true`() = runTest {
        // Given
        val expected = listOf(1 to true)
        coEvery { repository.isCartProducts() } returns flowOf(expected)

        // When
        val result = useCase().first()

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `should emit false when repository emits false`() = runTest {
        val expected = listOf(1 to false)
        coEvery { repository.isCartProducts() } returns flowOf(expected)

        val result = useCase().first()

        assertEquals(expected, result)
    }
}
