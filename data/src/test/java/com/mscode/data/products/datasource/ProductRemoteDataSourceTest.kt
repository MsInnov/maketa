package com.mscode.data.products.datasource

import com.mscode.data.products.api.ProductsApi
import com.mscode.data.products.model.ProductEntity
import com.mscode.domain.common.WrapperResults
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductRemoteDataSourceTest {

    private lateinit var api: ProductsApi
    private lateinit var dataSource: ProductRemoteDataSource

    @BeforeEach
    fun setup() {
        api = mockk()
        dataSource = ProductRemoteDataSource(api)
    }

    @Test
    fun `getProducts should return Success when API call is successful`() = runTest {
        // Given
        val expectedProducts = listOf(
            ProductEntity(1, "Phone", 1000.0, "desc", "cat", "img"),
            ProductEntity(2, "Laptop", 2000.0, "desc", "cat", "img")
        )
        coEvery { api.getProducts() } returns expectedProducts

        // When
        val result = dataSource.getProducts()

        // Then
        assertTrue(result is WrapperResults.Success)
        assertEquals(expectedProducts, (result as WrapperResults.Success).data)
    }

    @Test
    fun `getProducts should return Error when API throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { api.getProducts() } throws exception

        // When
        val result = dataSource.getProducts()

        // Then
        assertTrue(result is WrapperResults.Error)
        assertEquals(exception, (result as WrapperResults.Error).exception)
    }

    @Test
    fun `newProduct should return Success when API call is successful`() = runTest {
        // Given
        val products = ProductEntity(1, "Phone", 1000.0, "desc", "cat", "img")

        coEvery { api.newProduct(products)} returns products

        // When
        val result = dataSource.newProduct(products)

        // Then
        assertTrue(result is WrapperResults.Success)
        assertEquals(Unit, (result as WrapperResults.Success).data)
    }

    @Test
    fun `newProduct should return Error when API throws exception`() = runTest {
        // Given
        val products = ProductEntity(1, "Phone", 1000.0, "desc", "cat", "img")
        val exception = RuntimeException("Network error")
        coEvery { api.newProduct(products)} throws exception

        // When
        val result = dataSource.newProduct(products)

        // Then
        assertTrue(result is WrapperResults.Error)
        assertEquals(exception, (result as WrapperResults.Error).exception)
    }
}