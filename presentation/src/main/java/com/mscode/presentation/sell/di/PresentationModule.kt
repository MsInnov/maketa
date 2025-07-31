package com.mscode.presentation.sell.di

import com.mscode.presentation.sell.mapper.SellProductUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object PresentationModule {

    @Provides
    fun provideProductsUiMapper(): SellProductUiMapper = SellProductUiMapper()

}