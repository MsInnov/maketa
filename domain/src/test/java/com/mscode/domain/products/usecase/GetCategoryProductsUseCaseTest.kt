import com.mscode.domain.products.repository.ProductsRepository
import com.mscode.domain.products.usecase.GetCategoryProductsUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class GetCategoryProductsUseCaseTest {

    private val repository: ProductsRepository = mockk()
    private lateinit var useCase: GetCategoryProductsUseCase

    @BeforeEach
    fun setup() {
        useCase = GetCategoryProductsUseCase(repository)
    }

    @Test
    fun `invoke returns list with NONE prepended to categories`() {
        // Given
        val categories = listOf("Electronics", "Clothing", "Books")
        every { repository.getCategoryProducts() } returns categories

        // When
        val result = useCase()

        // Then
        val expected = listOf("NONE", "Electronics", "Clothing", "Books")
        assertEquals(expected, result)
    }

    @Test
    fun `invoke returns list with only NONE when repository returns empty list`() {
        // Given
        every { repository.getCategoryProducts() } returns emptyList()

        // When
        val result = useCase()

        // Then
        assertEquals(listOf("NONE"), result)
    }
}
