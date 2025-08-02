package com.mscode.data.user.repository

import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.model.Url
import com.mscode.data.remoteconfig.model.url_products
import com.mscode.data.user.api.UserApi
import com.mscode.data.user.datasource.UserRemoteDataSource
import com.mscode.data.user.model.UserEntity
import com.mscode.domain.common.WrapperResults
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {

    private val localConfigDataSource: LocalConfigDataSource = mockk()
    private val retrofitFactory: RetrofitFactory = mockk()
    private val userApi: UserApi = mockk()
    private lateinit var repository: UserRepositoryImpl

    private val testUrl = Url(url_products, "https://example.com")

    @BeforeEach
    fun setup() {
        repository = UserRepositoryImpl(localConfigDataSource, retrofitFactory)
    }

    @Test
    fun `getUser returns success when remote call succeeds`() = runTest {
        // Given
        every { localConfigDataSource.urls } returns listOf(testUrl).toMutableList()

        val expectedRemoteUser = UserEntity(id= 1, username = "john", email = "john@example.com", password = "pass")
        val remoteResult = WrapperResults.Success(expectedRemoteUser)

        // Mock RetrofitFactory to return mocked UserApi
        every { localConfigDataSource.urls } returns listOf(testUrl).toMutableList()
        every { retrofitFactory.create(testUrl.value, UserApi::class.java) } returns userApi
        mockkConstructor(UserRemoteDataSource::class)
        coEvery { anyConstructed<UserRemoteDataSource>().getUser() } returns remoteResult


        // When
        val result = repository.getUser()

        // Then
        assertTrue(result is WrapperResults.Success)
        val user = (result as WrapperResults.Success).data
        assertEquals("john", user.user)
        assertEquals("john@example.com", user.email)
    }

    @Test
    fun `getUser returns Error when product URL is missing`() = runTest {
        every { localConfigDataSource.urls } returns emptyList<Url>().toMutableList()

        val result = repository.getUser()

        kotlin.test.assertTrue(result is WrapperResults.Error)
        kotlin.test.assertEquals("Product URL missing", result.exception.message)
    }

    @Test
    fun `getUser returns error when remote call fails`() = runTest {
        // Given
        val api: UserApi = mockk()
        val exception = RuntimeException("Server error")

        every { localConfigDataSource.urls } returns listOf(testUrl).toMutableList()
        every { retrofitFactory.create(testUrl.value, UserApi::class.java) } returns api
        mockkConstructor(UserRemoteDataSource::class)
        coEvery { anyConstructed<UserRemoteDataSource>().getUser() } returns WrapperResults.Error(exception)


        // When
        val result = repository.getUser()

        // Then
        assertTrue(result is WrapperResults.Error)
        assertEquals("Server error", (result as WrapperResults.Error).exception.message)
    }
}