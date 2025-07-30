package com.mscode.domain.login.usecase

import com.mscode.domain.login.repository.LoginRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RemoveTokenUseCaseTest {

    private lateinit var repository: LoginRepository
    private lateinit var removeTokenUseCase: RemoveTokenUseCase

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = true)
        removeTokenUseCase = RemoveTokenUseCase(repository)
    }

    @Test
    fun `invoke should call repository removeToken`() {
        // When
        removeTokenUseCase()

        // Then
        verify(exactly = 1) { repository.removeToken() }
    }
}
