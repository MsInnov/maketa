package com.mscode.data.user.di

import com.mscode.data.user.repository.UserRepositoryImpl
import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.domain.user.repository.UserRepository
import com.mscode.domain.user.usecase.GetUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideAccountRepository(
        localConfigDataSource: LocalConfigDataSource,
        retrofitFactory: RetrofitFactory,
    ): UserRepository = UserRepositoryImpl(
        localConfigDataSource,
        retrofitFactory
    )

    @Provides
    @ViewModelScoped
    fun provideUserRemoteDataSource(
        repository: UserRepository
    ): GetUserUseCase = GetUserUseCase(
        repository
    )

}