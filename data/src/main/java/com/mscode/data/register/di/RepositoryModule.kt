package com.mscode.data.register.di

import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.register.repository.RegisterRepositoryImpl
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.domain.register.repository.RegisterRepository
import com.mscode.domain.register.usecase.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRegisterRepository(
        localConfigDataSource: LocalConfigDataSource,
        retrofit: RetrofitFactory
    ): RegisterRepository = RegisterRepositoryImpl(localConfigDataSource, retrofit)

    @Provides
    @Singleton
    fun provideRegisterUseCase(repo: RegisterRepository): RegisterUseCase =
        RegisterUseCase(repo)

}