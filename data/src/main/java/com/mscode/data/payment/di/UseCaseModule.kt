package com.mscode.data.payment.di

import com.mscode.domain.payment.usecase.VerifyBankInfosUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideVerifyBankInfosUseCase(): VerifyBankInfosUseCase =
        VerifyBankInfosUseCase()

}