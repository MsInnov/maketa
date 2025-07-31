package com.mscode.domain.login.usecase

import com.mscode.domain.login.repository.LoginRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetTokenUseCaseTest {

    private lateinit var repository: LoginRepository
    private lateinit var getTokenUseCase: GetTokenUseCase

    @BeforeEach
    fun setup() {
        repository = mockk()
        getTokenUseCase = GetTokenUseCase(repository)
    }

    @Test
    fun `should return token from repository`() {
        // Given
        val expectedToken = "abc123"
        every { repository.getToken() } returns expectedToken

        // When
        val result = getTokenUseCase()

        // Then
        assertEquals(expectedToken, result)
        verify(exactly = 1) { repository.getToken() }
    }
}
