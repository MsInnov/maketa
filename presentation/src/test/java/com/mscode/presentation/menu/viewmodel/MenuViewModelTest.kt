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
}
