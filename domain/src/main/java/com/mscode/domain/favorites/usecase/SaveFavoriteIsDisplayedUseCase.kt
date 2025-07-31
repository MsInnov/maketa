package com.mscode.domain.favorites.usecase

import com.mscode.domain.favorites.repository.FavoriteRepository

class SaveFavoriteIsDisplayedUseCase (
    private val favoritesRepository: FavoriteRepository
) {

    operator fun invoke(isDisplayed: Boolean) = favoritesRepository.saveFavoriteIsDisplayed(
        isDisplayed
    )

}