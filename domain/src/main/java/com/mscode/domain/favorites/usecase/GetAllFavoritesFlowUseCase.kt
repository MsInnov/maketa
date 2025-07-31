package com.mscode.domain.favorites.usecase

import com.mscode.domain.favorites.repository.FavoriteRepository

class GetAllFavoritesFlowUseCase (
    private val favoritesRepository: FavoriteRepository
) {

    operator fun invoke() = favoritesRepository.getFavoritesFlow()

}