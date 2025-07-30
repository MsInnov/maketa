package com.mscode.presentation.login.viewmodel

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.login.usecase.LoginUseCase
import com.mscode.presentation.login.model.UiEvent
import com.mscode.presentation.login.model.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val loginUseCase: LoginUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: LoginViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(loginUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEvent Login success should update uiState to Logged`() = runTest {
        // Given
        coEvery { loginUseCase("user", "pass") } returns WrapperResults.Success(Unit)

        // When
        viewModel.onEvent(UiEvent.Login("user", "pass"))
        testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(UiState.Logged, viewModel.uiState.value)
        coVerify(exactly = 1) { loginUseCase("user", "pass") }
    }

    @Test
    fun `onEvent Login error should update uiState to error mapped by toUiState`() = runTest {
        // Given
        val exception = Exception("Invalid credentials")
        coEvery { loginUseCase("user", "wrongpass") } returns WrapperResults.Error(exception)

        // simulate mapping from exception to UiState
        val expectedErrorState = UiState.Error // ← adapte selon ton extension `toUiState`
        // Si `toUiState` fait autre chose, remplace cette valeur

        // When
        viewModel.onEvent(UiEvent.Login("user", "wrongpass"))
        testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(expectedErrorState, viewModel.uiState.value)
        coVerify(exactly = 1) { loginUseCase("user", "wrongpass") }
    }

    @Test
    fun `onEvent Disconnect should update uiState to Idle`() = runTest {
        // When
        viewModel.onEvent(UiEvent.Disconnect)
        testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(UiState.Idle, viewModel.uiState.value)
    }
}
