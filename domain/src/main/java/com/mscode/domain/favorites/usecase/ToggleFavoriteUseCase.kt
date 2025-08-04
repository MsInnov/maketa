package com.mscode.domain.favorites.usecase

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.common.WrapperResults.*
import com.mscode.domain.favorites.repository.FavoriteRepository
import com.mscode.domain.products.model.Product
import java.lang.Exception

class ToggleFavoriteUseCase (
    private val favoritesRepository: FavoriteRepository
) {

    suspend operator fun invoke(
        favorite: Product.Favorite,
        isFavorite: Boolean
    ): WrapperResults<Unit> = if(isFavorite) {
        favoritesRepository.deleteFavorite(favorite).let { ret ->
            if(ret == 0) Error(Exception())
            else Success(Unit)
        }
    } else {
        favoritesRepository.addFavorite(favorite).let { ret ->
            if(ret == -1L) Error(Exception())
            else Success(Unit)
        }
    }

}