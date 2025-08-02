package com.mscode.data.cart.repository

import com.mscode.data.cart.datasource.CartLocalDataSource
import com.mscode.data.cart.mapper.CartMapper
import com.mscode.data.cart.model.CartProductEntity
import com.mscode.domain.cart.model.CartProduct
import com.mscode.domain.common.WrapperResults
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartRepositoryImplTest {

    private val cartLocalDataSource: CartLocalDataSource = mockk()
    private val mapper: CartMapper = mockk()
    private lateinit var repository: CartRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = CartRepositoryImpl(cartLocalDataSource, mapper)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addCartProduct should insert mapped product and return id`() = runTest {
        val product = CartProduct(1, "Test", 10.0, "desc", "cat", "img")
        val cartEntity = CartProductEntity(1, "Test", 10.0, "desc", "cat", "img")

        every { mapper.toCartLocalEntity(product) } returns cartEntity
        coEvery { cartLocalDataSource.insertCartProduct(cartEntity) } returns 42L

        val result = repository.addCartProduct(product)

        assertEquals(42L, result)
        coVerify { cartLocalDataSource.insertCartProduct(cartEntity) }
    }

    @Test
    fun `removeCartProduct should delete mapped product and return count`() = runTest {
        val product = CartProduct(1, "Test", 10.0, "desc", "cat", "img")
        val cartEntity = CartProductEntity(1, "Test", 10.0, "desc", "cat", "img")

        every { mapper.toCartLocalEntity(product) } returns cartEntity
        coEvery { cartLocalDataSource.deleteCartProduct(cartEntity) } returns 1

        val result = repository.removeCartProduct(product)

        assertEquals(1, result)
        coVerify { cartLocalDataSource.deleteCartProduct(cartEntity) }
    }

    @Test
    fun `getCart should return mapped list of cart products wrapped in success`() = runTest {
        val product = CartProduct(1, "Test", 10.0, "desc", "cat", "img")
        val cartEntity = CartProductEntity(1, "Test", 10.0, "desc", "cat", "img")
        val entities = listOf(cartEntity, cartEntity.copy(id=2))
        val products = listOf(product, product.copy(id=2))

        coEvery { cartLocalDataSource.getCart() } returns entities
        every { mapper.toCartProducts(entities) } returns products

        val result = repository.getCart()

        assertEquals(WrapperResults.Success(products), result)
    }

    @Test
    fun `removeCart should call deleteCart`() = runTest {
        coEvery { cartLocalDataSource.deleteCart() } just Runs

        repository.removeCart()

        coVerify { cartLocalDataSource.deleteCart() }
    }
}
