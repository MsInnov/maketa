package com.mscode.domain.favorites.usecase

import com.mscode.domain.favorites.repository.FavoriteRepository

class GetAllFavoritesUseCase(
    private val favoritesRepository: FavoriteRepository
) {

    suspend operator fun invoke() = favoritesRepository.getFavorites()

}