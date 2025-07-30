package com.mscode.data.products.repository

import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.products.api.ProductsApi
import com.mscode.data.products.datasource.ProductLocalDataSource
import com.mscode.data.products.datasource.ProductRemoteDataSource
import com.mscode.data.products.mapper.ProductsMapper
import com.mscode.data.products.model.ProductEntity
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.model.Url
import com.mscode.data.remoteconfig.model.url_products
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.products.model.Product
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import kotlin.test.*

class ProductsRepositoryImplTest {

    private lateinit var localConfigDataSource: LocalConfigDataSource
    private lateinit var retrofitFactory: RetrofitFactory
    private lateinit var productsMapper: ProductsMapper
    private lateinit var localProductsDataSource: ProductLocalDataSource
    private lateinit var repository: ProductsRepositoryImpl

    private val testUrl = Url(url_products, "https://example.com")

    @BeforeEach
    fun setUp() {
        localConfigDataSource = mockk()
        retrofitFactory = mockk()
        productsMapper = mockk()
        localProductsDataSource = mockk(relaxed = true)

        repository = ProductsRepositoryImpl(
            localConfigDataSource,
            retrofitFactory,
            productsMapper,
            localProductsDataSource
        )
    }

    @Test
    fun `getProducts returns Success when everything works`() = runTest {
        // Given
        val entity = ProductEntity(1, "Test", 10.0, "desc", "cat", "img")
        val product = Product(1, "Test", 10.0, "desc", "cat", "img", false)
        val api: ProductsApi = mockk()
        val remoteDataSource = mockk<ProductRemoteDataSource>()

        every { localConfigDataSource.urls } returns listOf(testUrl).toMutableList()
        every { retrofitFactory.create(testUrl.value, ProductsApi::class.java) } returns api
        every { productsMapper.toProducts(entity) } returns product
        coEvery { remoteDataSource.getProducts() } returns WrapperResults.Success(listOf(entity))
        mockkConstructor(ProductRemoteDataSource::class)
        coEvery { anyConstructed<ProductRemoteDataSource>().getProducts() } returns WrapperResults.Success(listOf(entity))

        // When
        val result = repository.getProducts()

        // Then
        assertTrue(result is WrapperResults.Success)
        assertEquals(listOf(product), (result as WrapperResults.Success).data)
        verify { localProductsDataSource.saveProducts(listOf(product)) }
    }

    @Test
    fun `getProducts returns Error when product URL is missing`() = runTest {
        every { localConfigDataSource.urls } returns emptyList<Url>().toMutableList()

        val result = repository.getProducts()

        assertTrue(result is WrapperResults.Error)
        assertEquals("Product URL missing", (result as WrapperResults.Error).exception.message)
    }

    @Test
    fun `getProducts returns Error when remote call fails`() = runTest {
        val api: ProductsApi = mockk()
        val exception = RuntimeException("Network error")

        every { localConfigDataSource.urls } returns listOf(testUrl).toMutableList()
        every { retrofitFactory.create(testUrl.value, ProductsApi::class.java) } returns api
        mockkConstructor(ProductRemoteDataSource::class)
        coEvery { anyConstructed<ProductRemoteDataSource>().getProducts() } returns WrapperResults.Error(exception)

        val result = repository.getProducts()

        assertTrue(result is WrapperResults.Error)
        assertEquals(exception, (result as WrapperResults.Error).exception)
    }
}
