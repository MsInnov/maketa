package com.mscode.data.products.di

import com.mscode.data.favorites.datasource.FavoriteLocalDataSource
import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.products.datasource.ProductLocalDataSource
import com.mscode.data.products.mapper.ProductsMapper
import com.mscode.data.products.repository.ProductsRepositoryImpl
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.domain.products.repository.ProductsRepository
import com.mscode.domain.products.usecase.GetProductsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideProductsMapper(): ProductsMapper = ProductsMapper()

    @Provides
    @Singleton
    fun provideProductsRepository(
        localConfigDataSource: LocalConfigDataSource,
        retrofit: RetrofitFactory,
        mapper: ProductsMapper,
        favoritesLocalDataSource: FavoriteLocalDataSource,
        localProductsDataSource: ProductLocalDataSource
    ): ProductsRepository = ProductsRepositoryImpl(localConfigDataSource, retrofit, mapper, favoritesLocalDataSource, localProductsDataSource)

    @Provides
    @Singleton
    fun provideLocalProductsDataSource(): ProductLocalDataSource = ProductLocalDataSource()

    @Provides
    @Singleton
    fun provideGetProductsUseCase(repo: ProductsRepository): GetProductsUseCase =
        GetProductsUseCase(repo)

}