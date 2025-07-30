package com.mscode.data.login.datasource

import com.mscode.data.login.api.LoginApi
import com.mscode.data.login.model.LoginBody
import com.mscode.data.login.model.LoginEntity
import com.mscode.domain.common.WrapperResults
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginRemoteDataSourceTest {

    private lateinit var api: LoginApi
    private lateinit var remoteDataSource: LoginRemoteDataSource

    @BeforeEach
    fun setup() {
        api = mockk()
        remoteDataSource = LoginRemoteDataSource(api)
    }

    @Test
    fun `login should return Success when API call succeeds`() = runTest {
        // Given
        val expectedEntity = LoginEntity(token = "abc123")
        val loginBody = LoginBody("user", "pass")
        coEvery { api.login(loginBody) } returns expectedEntity

        // When
        val result = remoteDataSource.login("user", "pass")

        // Then
        assertTrue(result is WrapperResults.Success)
        assertEquals(expectedEntity, (result as WrapperResults.Success).data)
        coVerify(exactly = 1) { api.login(loginBody) }
    }

    @Test
    fun `login should return Error when API call throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        val loginBody = LoginBody("user", "pass")
        coEvery { api.login(loginBody) } throws exception

        // When
        val result = remoteDataSource.login("user", "pass")

        // Then
        assertTrue(result is WrapperResults.Error)
        assertEquals(exception, result.exception)
        coVerify(exactly = 1) { api.login(loginBody) }
    }
}