package com.mscode.data.login.repository

import com.mscode.data.login.api.LoginApi
import com.mscode.data.login.datasource.LoginLocalDataSource
import com.mscode.data.login.model.LoginEntity
import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.model.Url
import com.mscode.data.remoteconfig.model.url_products
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.login.repository.LoginRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginRepositoryImplTest {

    private lateinit var localConfigDataSource: LocalConfigDataSource
    private lateinit var loginLocalDataSource: LoginLocalDataSource
    private lateinit var retrofitFactory: RetrofitFactory
    private lateinit var loginApi: LoginApi
    private lateinit var repository: LoginRepository

    private val baseUrl = "https://example.com"

    @BeforeEach
    fun setup() {
        localConfigDataSource = mockk()
        loginLocalDataSource = mockk(relaxed = true)
        retrofitFactory = mockk()
        loginApi = mockk()

        every { retrofitFactory.create(baseUrl, LoginApi::class.java) } returns loginApi

        repository = LoginRepositoryImpl(
            localConfigDataSource,
            loginLocalDataSource,
            retrofitFactory
        )
    }

    @Test
    fun `login should return Error when url_products is missing`() = runTest {
        every { localConfigDataSource.urls } returns mutableListOf()

        val result = repository.login("user", "pass")

        assertTrue(result is WrapperResults.Error)
        assertEquals("Product URL missing", (result as WrapperResults.Error).exception.message)
    }

    @Test
    fun `login should return Error when remote login fails`() = runTest {
        val error = Exception("401 Unauthorized")

        every {
            localConfigDataSource.urls
        } returns listOf(Url(key = url_products, value = baseUrl)).toMutableList()

        coEvery {
            loginApi.login(any())
        } throws error

        val result = repository.login("user", "pass")

        assertTrue(result is WrapperResults.Error)
        assertEquals(error, (result as WrapperResults.Error).exception)
    }

    @Test
    fun `login should save token and return Success when remote login succeeds`() = runTest {
        val token = "abc123"
        val entity = LoginEntity(token = token)

        every {
            localConfigDataSource.urls
        } returns listOf(Url(key = url_products, value = baseUrl)).toMutableList()

        coEvery {
            loginApi.login(any())
        } returns entity

        val result = repository.login("user", "pass")

        assertTrue(result is WrapperResults.Success)
        assertEquals(Unit, result.data)
        verify { loginLocalDataSource.saveToken(token) }
    }

    @Test
    fun `removeToken should call saveToken with null`() {
        repository.removeToken()

        verify(exactly = 1) { loginLocalDataSource.saveToken(null) }
    }
}
