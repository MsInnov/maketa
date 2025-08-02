package com.mscode.domain.user.usecase

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.user.model.User
import com.mscode.domain.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class GetUserUseCaseTest {

    private val repository: UserRepository = mockk()
    private lateinit var useCase: GetUserUseCase

    @BeforeEach
    fun setup() {
        useCase = GetUserUseCase(repository)
    }

    @Test
    fun `invoke returns success result from repository`() = runTest {
        // Given
        val expectedUser = User("john_doe", "john@example.com")
        coEvery { repository.getUser() } returns WrapperResults.Success(expectedUser)

        // When
        val result = useCase()

        // Then
        assertTrue(result is WrapperResults.Success)
        assertEquals(expectedUser, (result as WrapperResults.Success).data)
    }

    @Test
    fun `invoke returns error result from repository`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { repository.getUser() } returns WrapperResults.Error(exception)

        // When
        val result = useCase()

        // Then
        assertTrue(result is WrapperResults.Error)
        assertEquals(exception, (result as WrapperResults.Error).exception)
    }
}