package com.mscode.data.remoteconfig.repository

import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.datasource.RemoteConfigDataSource
import com.mscode.domain.common.WrapperResults
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertIs
import com.mscode.data.remoteconfig.model.Url
import org.junit.jupiter.api.Assertions.assertTrue


class RemoteConfigRepositoryImplTest {

    private lateinit var remoteConfigDataSource: RemoteConfigDataSource
    private lateinit var localConfigDataSource: LocalConfigDataSource
    private lateinit var repository: RemoteConfigRepositoryImpl

    @BeforeEach
    fun setup() {
        remoteConfigDataSource = mockk()
        localConfigDataSource = mockk(relaxed = true) // relaxed: pas besoin de définir saveUrl
        repository = RemoteConfigRepositoryImpl(remoteConfigDataSource, localConfigDataSource)
    }

    @Test
    fun `updateRemoteConfig should save all urls and return Success when data is available`() = runBlocking {
        val urls = listOf("google.com" to "search", "github.com" to "code")
        coEvery { remoteConfigDataSource.getRemoteConfig() } returns urls

        val result = repository.updateRemoteConfig()

        assertIs<WrapperResults.Success<Unit>>(result)
        urls.forEach {
            verify { localConfigDataSource.saveUrl(Url(it.first, it.second)) }
        }
        coVerify(exactly = 1) { remoteConfigDataSource.getRemoteConfig() }
    }

    @Test
    fun `updateRemoteConfig should return Error when data is null`() = runBlocking {
        coEvery { remoteConfigDataSource.getRemoteConfig() } returns null

        val result = repository.updateRemoteConfig()

        assertTrue(result is WrapperResults.Error)
        verify(exactly = 0) { localConfigDataSource.saveUrl(any()) }
        coVerify(exactly = 1) { remoteConfigDataSource.getRemoteConfig() }
    }
}
