package com.mscode.data.remoteconfig.di

import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.datasource.RemoteConfigDataSource
import com.mscode.data.remoteconfig.repository.RemoteConfigRepositoryImpl
import com.mscode.domain.remoteconfig.repository.RemoteConfigRepository
import com.mscode.domain.remoteconfig.usecase.GetRemoteConfigUseCase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
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
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(1000)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        return remoteConfig
    }

    @Provides
    @Singleton
    fun provideLocalConfigDataSource(): LocalConfigDataSource = LocalConfigDataSource()

    @Provides
    @Singleton
    fun provideRemoteConfigDataSource(firebaseRemoteConfig: FirebaseRemoteConfig): RemoteConfigDataSource =
        RemoteConfigDataSource(firebaseRemoteConfig)

    @Provides
    @Singleton
    fun provideRemoteConfigRepository(
        remote: RemoteConfigDataSource,
        local: LocalConfigDataSource
    ): RemoteConfigRepository = RemoteConfigRepositoryImpl(remote, local)

    @Provides
    @Singleton
    fun provideGetProductsUseCase(repo: RemoteConfigRepository): GetRemoteConfigUseCase =
        GetRemoteConfigUseCase(repo)
}