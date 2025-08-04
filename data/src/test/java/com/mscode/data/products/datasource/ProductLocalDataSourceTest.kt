package com.mscode.data.products.datasource

import com.mscode.domain.products.model.Product
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class ProductLocalDataSourceTest {

    private lateinit var dataSource: ProductLocalDataSource

    @BeforeEach
    fun setUp() {
        dataSource = ProductLocalDataSource()
    }

    @Test
    fun `products should be empty initially`() {
        assertTrue(dataSource.products.isEmpty())
    }

    @Test
    fun `saveProducts should update products list`() {
        // Given
        val product1 = Product.Classic(
            id = 1,
            title = "iPhone 15",
            price = 1299.99,
            description = "Latest Apple iPhone",
            category = "electronics",
            image = "https://example.com/iphone.jpg",
            isFavorite = false
        )

        val product2 = Product.Classic(
            id = 2,
            title = "Nike Air Max",
            price = 149.99,
            description = "Comfortable running shoes",
            category = "shoes",
            image = "https://example.com/nike.jpg",
            isFavorite = true
        )

        val newProducts = listOf(product1, product2)

        // When
        dataSource.saveProducts(newProducts)

        // Then
        val savedProducts = dataSource.products
        assertEquals(2, savedProducts.size)
        assertEquals("iPhone 15", savedProducts[0].title)
        assertEquals(149.99, savedProducts[1].price)
        assertTrue(savedProducts[1].isFavorite)
    }
}