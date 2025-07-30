package com.mscode.data.login.datasource

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoginLocalDataSourceTest {

    private lateinit var dataSource: LoginLocalDataSource

    @BeforeEach
    fun setup() {
        dataSource = LoginLocalDataSource()
    }

    @Test
    fun `token should be null by default`() {
        assertNull(dataSource.token)
    }

    @Test
    fun `saveToken should store and return token correctly`() {
        val expectedToken = "abc123"

        dataSource.saveToken(expectedToken)

        assertEquals(expectedToken, dataSource.token)
    }

    @Test
    fun `saveToken with null should clear the token`() {
        dataSource.saveToken("initial")
        dataSource.saveToken(null)

        assertNull(dataSource.token)
    }
}