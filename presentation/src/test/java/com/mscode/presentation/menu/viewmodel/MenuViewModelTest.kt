package com.mscode.presentation.menu.viewmodel

import com.mscode.domain.login.usecase.RemoveTokenUseCase
import com.mscode.presentation.menu.model.UiEvent
import com.mscode.presentation.menu.model.UiState
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
class MenuViewModelTest {

    private val removeTokenUseCase: RemoveTokenUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: MenuViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MenuViewModel(removeTokenUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEvent Selling should call removeTokenUseCase and update uiState to Selling`() = runTest {
        coEvery { removeTokenUseCase.invoke() } returns Unit

        viewModel.onEvent(UiEvent.Selling)

        testScheduler.runCurrent()

        assertEquals(UiState.Selling, viewModel.uiState.value)
    }

    @Test
    fun `onEvent Disconnect should call removeTokenUseCase and update uiState to Disconnected`() = runTest {
        coEvery { removeTokenUseCase.invoke() } returns Unit

        viewModel.onEvent(UiEvent.Disconnect)

        testScheduler.runCurrent()

        assertEquals(UiState.Disconnected, viewModel.uiState.value)
        coVerify(exactly = 1) { removeTokenUseCase.invoke() }
    }

    @Test
    fun `onEvent Idle should update uiState to Idle`() = runTest {
        viewModel.onEvent(UiEvent.Idle)

        testScheduler.runCurrent()

        assertEquals(UiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `onEvent Cart should update uiState to Cart`() = runTest {
        viewModel.onEvent(UiEvent.Cart)

        testScheduler.runCurrent()

        assertEquals(UiState.Cart, viewModel.uiState.value)
    }

    @Test
    fun `onEvent Account should update uiState to Account`() = runTest {
        viewModel.onEvent(UiEvent.Account)

        testScheduler.runCurrent()

        assertEquals(UiState.Account, viewModel.uiState.value)
    }
}
