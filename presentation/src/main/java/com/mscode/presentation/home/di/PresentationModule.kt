package com.mscode.presentation.home.di

import com.mscode.presentation.home.mapper.ProductsUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object PresentationModule {

    @Provides
    fun provideProductsUiMapper(): ProductsUiMapper = ProductsUiMapper()

}