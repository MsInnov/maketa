package com.mscode.presentation.cart.di

import com.mscode.presentation.cart.mapper.CartProductUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object PresentationModule {

    @Provides
    fun providePurchaseProductUiMapper(): CartProductUiMapper = CartProductUiMapper()

}