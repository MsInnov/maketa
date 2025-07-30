package com.mscode.domain.products.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.products.model.Product
import com.mscode.domain.products.repository.ProductsRepository

class GetProductsUseCaseTest {

    private val repository: ProductsRepository = mockk()
    private val useCase = GetProductsUseCase(repository)

    @Test
    fun `invoke should return Success result from repository`() = runTest {
        // Given
        val productList = listOf(
            Product(
                id = 1,
                title = "Test Product",
                price = 19.99,
                description = "A test product",
                category = "test",
                image = "http://example.com/image.png",
                isFavorite = false
            )
        )
        val expectedResult = WrapperResults.Success(productList)
        coEvery { repository.getProducts() } returns expectedResult

        // When
        val result = useCase()

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `invoke should return Error result from repository`() = runTest {
        // Given
        val expectedResult = WrapperResults.Error(Exception("Network error"))
        coEvery { repository.getProducts() } returns expectedResult

        // When
        val result = useCase()

        // Then
        assertEquals(expectedResult, result)
    }
}