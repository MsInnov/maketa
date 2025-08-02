package com.mscode.data.user.datasource

import com.mscode.data.user.api.UserApi
import com.mscode.data.user.model.UserEntity
import com.mscode.domain.common.WrapperResults
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class UserRemoteDataSourceTest {

    private lateinit var api: UserApi
    private lateinit var dataSource: UserRemoteDataSource

    @BeforeEach
    fun setup() {
        api = mockk()
        dataSource = UserRemoteDataSource(api)
    }

    @Test
    fun `getProducts should return Success when API call is successful`() = runTest {
        // Given
        val entity = UserEntity(1, "so", "so@gmail.fr", "desc")
        val expectedUser = Response.success(entity)
        coEvery { api.getUser(1) } returns expectedUser

        // When
        val result = dataSource.getUser()

        // Then
        assertTrue(result is WrapperResults.Success)
        assertEquals(entity, (result as WrapperResults.Success).data)
    }

    @Test
    fun `getProducts should return Error when API throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { api.getUser(1) } throws exception

        // When
        val result = dataSource.getUser()

        // Then
        assertTrue(result is WrapperResults.Error)
        assertEquals(exception, (result as WrapperResults.Error).exception)
    }

}