package com.mscode.presentation.account.viewmodel

import app.cash.turbine.test
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.user.usecase.GetUserUseCase
import com.mscode.domain.user.model.User
import com.mscode.presentation.account.model.UiEvent
import com.mscode.presentation.account.model.UiState
import com.mscode.presentation.account.model.UiState.Idle
import io.mockk.coEvery
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

class AccountViewModelTest {

    private val getUserUseCase: GetUserUseCase = mockk()

    private lateinit var viewModel: AccountViewModel

    private val user = User("jean", "poulin")
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AccountViewModel(getUserUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEvent GetProfile with Success result should emit Success state`() = runTest {
        coEvery { getUserUseCase() } returns WrapperResults.Success(user)

        viewModel.uiState.test {
            assertEquals(Idle, awaitItem())
            viewModel.onEvent(UiEvent.GetProfile)
            assertEquals(UiState.Profile("jean", "poulin"), awaitItem())
        }
    }

    @Test
    fun `onEvent GetProfile with Error result should emit Error state`() = runTest {
        coEvery { getUserUseCase() } returns WrapperResults.Error(Exception())

        viewModel.uiState.test {
            assertEquals(Idle, awaitItem())
            viewModel.onEvent(UiEvent.GetProfile)
            assertEquals(UiState.Error, awaitItem())
        }
    }

}