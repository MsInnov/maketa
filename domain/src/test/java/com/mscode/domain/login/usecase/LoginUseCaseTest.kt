package com.mscode.domain.login.usecase

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.login.model.LoginError
import com.mscode.domain.login.repository.LoginRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class LoginUseCaseTest {

    private lateinit var repository: LoginRepository
    private lateinit var loginUseCase: LoginUseCase

    @BeforeEach
    fun setup() {
        repository = mockk()
        loginUseCase = LoginUseCase(repository)
    }

    @Test
    fun `invoke should return Error(Exception) when login and password are blank`() = runTest {
        val result = loginUseCase("", "")
        assertTrue(result is WrapperResults.Error && result.exception is Exception)
    }

    @Test
    fun `invoke should return ErrorLogin when login is blank`() = runTest {
        val result = loginUseCase("", "password")
        assertTrue(result is WrapperResults.Error && result.exception == LoginError.ErrorLogin)
    }

    @Test
    fun `invoke should return ErrorPass when password is blank`() = runTest {
        val result = loginUseCase("user", "")
        assertTrue(result is WrapperResults.Error && result.exception == LoginError.ErrorPass)
    }

    @Test
    fun `invoke should call repository and return result when login and password are not blank`() = runTest {
        val expectedResult = WrapperResults.Success(Unit)
        coEvery { repository.login("user", "pass") } returns expectedResult

        val result = loginUseCase("user", "pass")

        assertTrue(result === expectedResult)
        coVerify(exactly = 1) { repository.login("user", "pass") }
    }
}