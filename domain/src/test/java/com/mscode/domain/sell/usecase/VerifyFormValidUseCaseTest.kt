package com.mscode.domain.sell.usecase

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VerifyFormValidUseCaseTest {

    private val useCase = VerifyFormValidUseCase()

    @Test
    fun `valid input returns no errors`() {
        val result = useCase(
            title = "Titre",
            description = "Description valide",
            price = "10.5",
            category = "Catégorie",
            imageUrl = "http://image.url"
        )

        assertTrue(result.isValid)
        assertFalse(result.titleError)
        assertFalse(result.descriptionError)
        assertFalse(result.priceError)
        assertFalse(result.categoryError)
        assertFalse(result.imageUrlError)
    }

    @Test
    fun `empty fields return errors`() {
        val result = useCase(
            title = "",
            description = "",
            price = "",
            category = "",
            imageUrl = ""
        )

        assertFalse(result.isValid)
        assertTrue(result.titleError)
        assertTrue(result.descriptionError)
        assertTrue(result.priceError)
        assertTrue(result.categoryError)
        assertTrue(result.imageUrlError)
    }

    @Test
    fun `invalid price returns price error`() {
        val result = useCase(
            title = "Titre",
            description = "Description",
            price = "-5",
            category = "Catégorie",
            imageUrl = "http://image.url"
        )

        assertFalse(result.isValid)
        assertFalse(result.titleError)
        assertFalse(result.descriptionError)
        assertTrue(result.priceError)
        assertFalse(result.categoryError)
        assertFalse(result.imageUrlError)
    }

    @Test
    fun `non numeric price returns price error`() {
        val result = useCase(
            title = "Titre",
            description = "Description",
            price = "abc",
            category = "Catégorie",
            imageUrl = "http://image.url"
        )

        assertFalse(result.isValid)
        assertTrue(result.priceError)
    }
}
