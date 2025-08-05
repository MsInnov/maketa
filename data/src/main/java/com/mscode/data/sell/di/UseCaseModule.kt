package com.mscode.data.sell.di

import com.mscode.domain.sell.usecase.VerifyFormValidUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideVerifyFromValidUseCase(): VerifyFormValidUseCase =
        VerifyFormValidUseCase()

}