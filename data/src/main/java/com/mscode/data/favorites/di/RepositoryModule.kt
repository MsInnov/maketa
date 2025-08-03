package com.mscode.data.favorites.di

import android.content.Context
import androidx.room.Room
import com.mscode.data.cart.datasource.CartLocalDataSource
import com.mscode.data.favorites.datasource.AppDatabase
import com.mscode.data.favorites.datasource.FavoriteLocalDataSource
import com.mscode.data.favorites.datasource.ProductDao
import com.mscode.data.favorites.mapper.FavoriteMapper
import com.mscode.data.favorites.repository.FavoriteRepositoryImpl
import com.mscode.domain.favorites.repository.FavoriteRepository
import com.mscode.domain.favorites.usecase.GetAllFavoritesFlowUseCase
import com.mscode.domain.favorites.usecase.GetAllFavoritesUseCase
import com.mscode.domain.favorites.usecase.SaveFavoriteIsDisplayedUseCase
import com.mscode.domain.favorites.usecase.ToggleFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "favorites_database"
        ).build()

    @Provides
    @Singleton
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()

    @Provides
    @Singleton
    fun provideFavoriteLocalDataSource(dao: ProductDao) = FavoriteLocalDataSource(dao)

    @Provides
    fun provideFavoritesMapper(): FavoriteMapper = FavoriteMapper()

    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(
        favoriteRepository: FavoriteRepository
    ): ToggleFavoriteUseCase = ToggleFavoriteUseCase(
        favoriteRepository
    )

    @Provides
    @Singleton
    fun provideSetFavoriteIsDisplayedUseCase(
        favoritesRepository: FavoriteRepository
    ): SaveFavoriteIsDisplayedUseCase = SaveFavoriteIsDisplayedUseCase (
        favoritesRepository
    )

    @Provides
    @Singleton
    fun provideGetAllFavoritesUseCase(
        favoriteRepository: FavoriteRepository
    ): GetAllFavoritesUseCase = GetAllFavoritesUseCase(
        favoriteRepository
    )

    @Provides
    @Singleton
    fun provideGetAllFavoritesFlowUseCase(
        favoriteRepository: FavoriteRepository
    ): GetAllFavoritesFlowUseCase = GetAllFavoritesFlowUseCase(
        favoriteRepository
    )

    @Provides
    @Singleton
    fun provideProductRepository(
        favoriteLocalDataSource: FavoriteLocalDataSource,
        cartLocalDataSource: CartLocalDataSource,
        favoriteMapper: FavoriteMapper
    ): FavoriteRepository = FavoriteRepositoryImpl(
        favoriteLocalDataSource = favoriteLocalDataSource,
        cartLocalDataSource = cartLocalDataSource,
        favoriteMapper = favoriteMapper
    )
}