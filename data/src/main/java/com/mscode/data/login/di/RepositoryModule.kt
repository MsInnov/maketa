package com.mscode.data.login.di

import com.mscode.data.login.datasource.LoginLocalDataSource
import com.mscode.data.login.repository.LoginRepositoryImpl
import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.domain.login.repository.LoginRepository
import com.mscode.domain.login.usecase.LoginUseCase
import com.mscode.domain.login.usecase.RemoveTokenUseCase
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
    fun provideLoginRepository(
        localConfigDataSource: LocalConfigDataSource,
        loginLocalDataSource: LoginLocalDataSource,
        retrofit: RetrofitFactory
    ): LoginRepository = LoginRepositoryImpl(localConfigDataSource, loginLocalDataSource, retrofit)

    @Provides
    @Singleton
    fun provideLoginUseCase(repo: LoginRepository): LoginUseCase =
        LoginUseCase(repo)

    @Provides
    @Singleton
    fun provideRemoveTokenUseCase(repo: LoginRepository): RemoveTokenUseCase =
        RemoveTokenUseCase(repo)

    @Provides
    @Singleton
    fun provideLoginLocalDataSource(): LoginLocalDataSource = LoginLocalDataSource()

}