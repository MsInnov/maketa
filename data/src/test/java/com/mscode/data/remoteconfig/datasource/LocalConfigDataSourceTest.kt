package com.mscode.data.remoteconfig.datasource

import com.mscode.data.remoteconfig.model.Url
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalConfigDataSourceTest {

    private lateinit var dataSource: LocalConfigDataSource

    @BeforeEach
    fun setUp() {
        dataSource = LocalConfigDataSource()
    }

    @Test
    fun `saveUrl should add a new Url to the list`() {
        // Given
        val url = Url("search", "https://google.com")

        // When
        dataSource.saveUrl(url)

        // Then
        assertEquals(1, dataSource.urls.size)
        assertEquals(url, dataSource.urls[0])
    }

    @Test
    fun `urls should initially be empty`() {
        assertTrue(dataSource.urls.isEmpty())
    }
}
