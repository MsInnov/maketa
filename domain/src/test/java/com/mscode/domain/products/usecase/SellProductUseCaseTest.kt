package com.mscode.domain.products.usecase

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.products.model.Product
import com.mscode.domain.products.repository.ProductsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SellProductUseCaseTest {

    private val repository: ProductsRepository = mockk()
    private lateinit var useCase: SellProductUseCase

    private val product = Product.Classic(1, "Title", 10.0, "Desc", "Cat", "Img", false)

    @BeforeEach
    fun setup() {
        useCase = SellProductUseCase(repository)
    }

    @Test
    fun `invoke should return success from repository`() = runTest {
        val expected = WrapperResults.Success(Unit)
        coEvery { repository.sellProduct(product) } returns expected

        val result = useCase(product)

        assertEquals(expected, result)
        coVerify { repository.sellProduct(product) }
    }

    @Test
    fun `invoke should return error from repository`() = runTest {
        val error = WrapperResults.Error(Exception("erreur"))
        coEvery { repository.sellProduct(product) } returns error

        val result = useCase(product)

        assertEquals(error, result)
        coVerify { repository.sellProduct(product) }
    }
}