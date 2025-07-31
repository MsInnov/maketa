package com.mscode.data.favorites.datasource

import com.mscode.data.favorites.model.FavoriteEntity
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FavoriteLocalDataSourceTest {

    private lateinit var dao: ProductDao
    private lateinit var dataSource: FavoriteLocalDataSource

    private val entity = FavoriteEntity(1, "Title", 10.0, "Desc", "Cat", "Img")

    @BeforeEach
    fun setUp() {
        dao = mockk()
        dataSource = FavoriteLocalDataSource(dao)
    }

    @Test
    fun `getAllFavorites should return list from dao`() = runTest {
        coEvery { dao.getAll() } returns listOf(entity)

        val result = dataSource.getAllFavorites()

        assertEquals(listOf(entity), result)
    }

    @Test
    fun `getFavoriteById should return result from dao`() = runTest {
        coEvery { dao.getById(1) } returns entity

        val result = dataSource.getFavoriteById(1)

        assertEquals(entity, result)
    }

    @Test
    fun `getFavoriteById should return null if not found`() = runTest {
        coEvery { dao.getById(999) } returns null

        val result = dataSource.getFavoriteById(999)

        assertNull(result)
    }

    @Test
    fun `insertFavorite should call dao insert`() = runTest {
        coEvery { dao.insert(entity) } returns 42L

        val result = dataSource.insertFavorite(entity)

        assertEquals(42L, result)
        coVerify { dao.insert(entity) }
    }

    @Test
    fun `deleteFavorite should call dao delete`() = runTest {
        coEvery { dao.delete(entity) } returns 1

        val result = dataSource.deleteFavorite(entity)

        assertEquals(1, result)
        coVerify { dao.delete(entity) }
    }

    @Test
    fun `getAllFavoritesFlow should return flow from dao`() = runTest {
        every { dao.getAllFlow() } returns flowOf(listOf(entity))

        val result = dataSource.getAllFavoritesFlow()

        assertEquals(listOf(entity), result.first())
    }

    @Test
    fun `saveFavoriteIsDisplayed should store and return value`() {
        assertEquals(false, dataSource.isDisplayed)

        dataSource.saveFavoriteIsDisplayed(true)

        assertTrue(dataSource.isDisplayed)
    }
}
