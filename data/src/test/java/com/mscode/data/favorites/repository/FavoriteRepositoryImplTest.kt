package com.mscode.data.favorites.repository

import com.mscode.data.cart.datasource.CartLocalDataSource
import com.mscode.data.favorites.datasource.FavoriteLocalDataSource
import com.mscode.data.favorites.mapper.FavoriteMapper
import com.mscode.data.favorites.model.FavoriteEntity
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.favorites.model.FavoriteProduct
import com.mscode.domain.products.model.Product
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FavoriteRepositoryImplTest {

    private lateinit var dataSource: FavoriteLocalDataSource
    private lateinit var mapper: FavoriteMapper
    private lateinit var repository: FavoriteRepositoryImpl
    private lateinit var cartLocalDataSource: CartLocalDataSource

    private val favoriteProducts = FavoriteProduct(1, "Title", 10.0, "Desc", "Cat", "Img", false)
    private val entity = FavoriteEntity(1, "Title", 10.0, "Desc", "Cat", "Img")
    private val product = Product(1, "Title", 10.0, "Desc", "Cat", "Img", false)

    @BeforeEach
    fun setUp() {
        dataSource = mockk()
        mapper = mockk()
        cartLocalDataSource = mockk()
        repository = FavoriteRepositoryImpl(dataSource, cartLocalDataSource, mapper)
    }

    @Test
    fun `addFavorites should insert mapped entity`() = runTest {
        coEvery { mapper.toProductsEntity(favoriteProducts) } returns entity
        coEvery { dataSource.insertFavorite(entity) } returns 5L

        val result = repository.addFavorites(favoriteProducts)

        assertEquals(5L, result)
        coVerify { dataSource.insertFavorite(entity) }
    }

    @Test
    fun `deleteFavorites should delete mapped entity`() = runTest {
        coEvery { mapper.toProductsEntity(favoriteProducts) } returns entity
        coEvery { dataSource.deleteFavorite(entity) } returns 1

        val result = repository.deleteFavorites(favoriteProducts)

        assertEquals(1, result)
        coVerify { dataSource.deleteFavorite(entity) }
    }

    @Test
    fun `getFavorites should map all entities to domain model`() = runTest {
        val entities = listOf(entity)
        coEvery { dataSource.getAllFavorites() } returns entities
        coEvery { cartLocalDataSource.getCartProductById(any()) } returns null
        coEvery { mapper.toFavoriteProducts(entity, false) } returns favoriteProducts

        val result = repository.getFavorites()

        assertEquals(listOf(favoriteProducts), result)
    }

    @Test
    fun `getFavorite should return Success when entity exists`() = runTest {
        coEvery { dataSource.getFavoriteById(1) } returns entity
        coEvery { cartLocalDataSource.getCartProductById(any()) } returns null
        coEvery { mapper.toFavoriteProducts(entity, false) } returns favoriteProducts

        val result = repository.getFavorite(1)

        assertTrue(result is WrapperResults.Success)
        assertEquals(favoriteProducts, result.data)
    }

    @Test
    fun `getFavorite should return Error when entity is null`() = runTest {
        coEvery { dataSource.getFavoriteById(1) } returns null

        val result = repository.getFavorite(1)

        assertTrue(result is WrapperResults.Error)
    }

    @Test
    fun `saveFavoriteIsDisplayed should call localDataSource`() {
        every { dataSource.saveFavoriteIsDisplayed(true) } just Runs

        repository.saveFavoriteIsDisplayed(true)

        verify { dataSource.saveFavoriteIsDisplayed(true) }
    }

    @Test
    fun `getIsDisplayed should return value from localDataSource`() {
        every { dataSource.isDisplayed } returns true

        val result = repository.getIsDisplayed()

        assertEquals(true, result)
    }

    @Test
    fun `getFavoritesFilteredByCategory should filter and map correctly`() = runTest {
        val matching = entity
        val nonMatching = entity.copy(category = "othercat")
        val entities = listOf(matching, nonMatching)

        coEvery { dataSource.getAllFavorites() } returns entities
        coEvery { mapper.toProducts(matching) } returns product

        val result = repository.getFavoritesFilteredByCategory("Cat")

        assertEquals(listOf(product), result)
    }

    @Test
    fun `getFavoritesFilteredByCategoryFlow should filter and map correctly`() = runTest {
        val matching = entity
        val nonMatching = entity.copy(category = "othercat")
        every { dataSource.getAllFavoritesFlow() } returns flowOf(listOf(matching, nonMatching))
        every { mapper.toProducts(matching) } returns product

        val flow = repository.getFavoritesFilteredByCategoryFlow("Cat")
        val result = flow.first()

        assertEquals(listOf(product), result)
    }

    @Test
    fun `getFavoritesFlow should map entity list to domain list`() = runTest {
        every { dataSource.getAllFavoritesFlow() } returns flowOf(listOf(entity))
        coEvery { cartLocalDataSource.getCartProductById(any()) } returns null
        every { mapper.toFavoriteProducts(entity, false) } returns favoriteProducts

        val flow = repository.getFavoritesFlow()
        val result = flow.first()

        assertEquals(listOf(favoriteProducts), result)
    }
}