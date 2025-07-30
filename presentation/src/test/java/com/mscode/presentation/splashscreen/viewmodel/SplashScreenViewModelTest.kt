package com.mscode.presentation.splashscreen.viewmodel

import app.cash.turbine.test
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.remoteconfig.usecase.GetRemoteConfigUseCase
import com.mscode.presentation.splashscreen.model.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SplashScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GetRemoteConfigUseCase
    private lateinit var viewModel: SplashScreenViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState should emit Success and isReady true after delay and successful remote config`() = runTest {
        // Arrange
        coEvery { useCase() } returns WrapperResults.Success(Unit)

        viewModel = SplashScreenViewModel(useCase)

        // Act & Assert
        viewModel.uiState.test {
            // Initial state is Pending
            assertEquals(UiState.Pending, awaitItem())

            // Advance time for coroutine delay(3000)
            advanceTimeBy(3000)

            // Expect Success state emitted
            assertEquals(UiState.Success, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        viewModel.isReady.test {
            // Initially false
            assertEquals(false, awaitItem())

            // Advance time for delay (already done above, but double-check)
            advanceTimeBy(3000)

            // isReady should be true after delay and success
            assertEquals(true, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState should emit Error when remote config fails`() = runTest {
        // Arrange
        coEvery { useCase() } returns WrapperResults.Error(Exception("Failed"))

        viewModel = SplashScreenViewModel(useCase)

        viewModel.uiState.test {
            assertEquals(UiState.Pending, awaitItem())

            advanceUntilIdle()

            assertEquals(UiState.Error, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        viewModel.isReady.test {
            // isReady should never be true on failure
            assertEquals(false, awaitItem())

            advanceUntilIdle()

            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }
    }
}