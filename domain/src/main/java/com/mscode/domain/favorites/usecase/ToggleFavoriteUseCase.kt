package com.mscode.domain.favorites.usecase

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.common.WrapperResults.*
import com.mscode.domain.favorites.model.FavoriteProduct
import com.mscode.domain.favorites.repository.FavoriteRepository
import java.lang.Exception

class ToggleFavoriteUseCase (
    private val favoritesRepository: FavoriteRepository
) {

    suspend operator fun invoke(
        favoriteProducts: FavoriteProduct,
        isFavoriteProducts: Boolean
    ): WrapperResults<Unit> = if(isFavoriteProducts) {
        favoritesRepository.deleteFavorites((favoriteProducts)).let { ret ->
            if(ret == 0) Error(Exception())
            else Success(Unit)
        }
    } else {
        favoritesRepository.addFavorites(favoriteProducts).let { ret ->
            if(ret == -1L) Error(Exception())
            else Success(Unit)
        }
    }

}