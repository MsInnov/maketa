package com.mscode.domain.remoteconfig.usecase

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.remoteconfig.repository.RemoteConfigRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetRemoteConfigUseCaseTest {

    private lateinit var repository: RemoteConfigRepository
    private lateinit var useCase: GetRemoteConfigUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = GetRemoteConfigUseCase(repository)
    }

    @Test
    fun `invoke should return success when repository returns success`() = runTest {
        // Given
        coEvery { repository.updateRemoteConfig() } returns WrapperResults.Success(Unit)

        // When
        val result = useCase()

        // Then
        assertTrue(result is WrapperResults.Success)
        coVerify(exactly = 1) { repository.updateRemoteConfig() }
    }

    @Test
    fun `invoke should return error when repository returns error`() = runTest {
        // Given
        coEvery { repository.updateRemoteConfig() } returns WrapperResults.Error(Exception("Something went wrong"))

        // When
        val result = useCase()

        // Then
        assertTrue(result is WrapperResults.Error)
        coVerify(exactly = 1) { repository.updateRemoteConfig() }
    }
}