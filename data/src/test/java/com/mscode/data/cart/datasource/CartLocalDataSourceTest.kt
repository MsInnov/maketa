package com.mscode.data.cart.datasource

import com.mscode.data.cart.model.CartProductEntity
import io.mockk.Awaits
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartLocalDataSourceTest {

    private val dao: CartDao = mockk()
    private lateinit var dataSource: CartLocalDataSource

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        dataSource = CartLocalDataSource(dao)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCart should call dao getCart and return result`() = runTest {
        val cartEntity = CartProductEntity(1, "Test", 10.0, "desc", "cat", "img")
        val expected = listOf(cartEntity)
        coEvery { dao.getCart() } returns expected

        val result = dataSource.getCart()

        assertEquals(expected, result)
        coVerify { dao.getCart() }
    }

    @Test
    fun `getCartByFlow should return dao flow`() {
        val cartEntity = CartProductEntity(1, "Test", 10.0, "desc", "cat", "img")
        val flow = flowOf(listOf(cartEntity))
        every { dao.getCartByFlow() } returns flow

        val result = dataSource.getCartByFlow()

        assertEquals(flow, result)
        verify { dao.getCartByFlow() }
    }

    @Test
    fun `insertCartProduct should call dao insert`() = runTest {
        val cartEntity = CartProductEntity(1, "Test", 10.0, "desc", "cat", "img")
        coEvery { dao.insertCartProduct(cartEntity) } returns 42L

        dataSource.insertCartProduct(cartEntity)

        coVerify { dao.insertCartProduct(cartEntity) }
    }

    @Test
    fun `deleteCartProduct should call dao delete`() = runTest {
        val cartEntity = CartProductEntity(1, "Test", 10.0, "desc", "cat", "img")
        coEvery { dao.deleteCartProduct(cartEntity) } returns 1

        dataSource.deleteCartProduct(cartEntity)

        coVerify { dao.deleteCartProduct(cartEntity) }
    }

    @Test
    fun `deleteCart should call dao deleteCart`() = runTest {
        coEvery { dao.deleteCart() } just Runs

        dataSource.deleteCart()

        coVerify { dao.deleteCart() }
    }

}
