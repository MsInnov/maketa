package com.mscode.data.remoteconfig.datasource

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.mscode.data.remoteconfig.extension.toUrl
import com.mscode.data.remoteconfig.model.urls
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull

class RemoteConfigDataSourceTest {

    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var dataSource: RemoteConfigDataSource

    @BeforeEach
    fun setUp() {
        remoteConfig = mockk(relaxed = true)
        dataSource = RemoteConfigDataSource(remoteConfig)
    }

    @Test
    fun `getRemoteConfig should return list when Fetch Is Successful`() = runTest {
        // Given
        val mockTask: Task<Boolean> = mockk(relaxed = true)
        every { mockTask.isSuccessful } returns true
        every { remoteConfig.fetchAndActivate() } returns mockTask
        // Mock URLs
        urls.forEach {
            every { remoteConfig.getString(it) } returns "\"https://example.com\""
        }

        // Simuler appel au listener
        every {
            mockTask.addOnCompleteListener(any())
        } answers {
            val listener = arg<OnCompleteListener<Boolean>>(0)
            listener.onComplete(mockTask)
            mockTask
        }

        // When
        val result = dataSource.getRemoteConfig()

        // Then
        assertNotNull(result)
        assertEquals(urls.size, result!!.size)
        result.forEach {
            assertEquals("\"https://example.com\"".toUrl, it.second)
        }
    }

    @Test
    fun `getRemoteConfig should return null when Fetch fails`() = runTest {
        // Given
        val mockTask: Task<Boolean> = mockk(relaxed = true)
        every { mockTask.isSuccessful } returns false
        every { remoteConfig.fetchAndActivate() } returns mockTask

        every {
            mockTask.addOnCompleteListener(any())
        } answers {
            val listener = arg<OnCompleteListener<Boolean>>(0)
            listener.onComplete(mockTask)
            mockTask
        }

        // When
        val result = dataSource.getRemoteConfig()

        // Then
        assertNull(result)
    }
}
