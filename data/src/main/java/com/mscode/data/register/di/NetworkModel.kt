package com.mscode.data.register.di

import com.mscode.data.register.api.RegisterApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRegisterApi(retrofit: Retrofit): RegisterApi =
        retrofit.create(RegisterApi::class.java)
}