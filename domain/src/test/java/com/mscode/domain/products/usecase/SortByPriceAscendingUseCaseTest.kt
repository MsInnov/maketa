package com.mscode.domain.products.usecase

import com.mscode.domain.products.model.Product
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SortByPriceAscendingUseCaseTest {

    private lateinit var useCase: SortByPriceAscendingUseCase

    private val product = Product.Classic(
        id = 1,
        title = "Test Product",
        price = 19.99,
        description = "A test product",
        category = "test",
        image = "http://example.com/image.png",
        isFavorite = false
    )

    @BeforeEach
    fun setup() {
        useCase = SortByPriceAscendingUseCase()
    }

    @Test
    fun `invoke returns products sorted by price in ascending order`() {
        // Given
        val products = listOf(
            product.copy(id = 1, price = 15.0),
            product.copy(id = 2, price = 5.0),
            product.copy(id = 3, price = 10.0)
        )

        // When
        val result = useCase(products)

        // Then
        val expected = listOf(
            product.copy(id = 2,  price = 5.0),
            product.copy(id = 3,  price = 10.0),
            product.copy(id = 1,  price = 15.0)
        )

        assertEquals(expected, result)
    }

    @Test
    fun `invoke returns empty list when input is empty`() {
        val result = useCase(emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke returns same list when all prices are equal`() {
        val products = listOf(
            product.copy(id = 1, price = 20.0),
            product.copy(id = 2, price = 20.0),
            product.copy(id = 3, price = 20.0)
        )

        val result = useCase(products)
        assertEquals(products, result)
    }
}
