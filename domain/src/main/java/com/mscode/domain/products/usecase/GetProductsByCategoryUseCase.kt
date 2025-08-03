package com.mscode.domain.products.usecase

import com.mscode.domain.favorites.repository.FavoriteRepository
import com.mscode.domain.products.repository.ProductsRepository
import kotlinx.coroutines.flow.flowOf

class GetProductsByCategoryUseCase(
    private val productsRepository: ProductsRepository,
    private val favoritesRepository: FavoriteRepository
) {

    companion object{
        private const val NONE = "NONE"
    }

    suspend operator fun invoke(category: String) = if (category != NONE) {
        if(favoritesRepository.getIsDisplayed()) {
            favoritesRepository.getFavoritesFilteredByCategoryFlow(category)
        } else {
            flowOf(productsRepository.getProductsFilteredByCategory(category))
        }
    } else {
        flowOf(emptyList())
    }

}