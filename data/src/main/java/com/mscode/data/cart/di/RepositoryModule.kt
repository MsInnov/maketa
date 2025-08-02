package com.mscode.data.cart.di

import android.content.Context
import androidx.room.Room
import com.mscode.data.cart.datasource.AppDatabase
import com.mscode.data.cart.datasource.CartLocalDataSource
import com.mscode.data.cart.datasource.CartDao
import com.mscode.data.cart.mapper.CartMapper
import com.mscode.data.cart.repository.CartRepositoryImpl
import com.mscode.domain.cart.repository.CartRepository
import com.mscode.domain.cart.usecase.GetCartUseCase
import com.mscode.domain.cart.usecase.RemoveCartUseCase
import com.mscode.domain.cart.usecase.RemoveCartProductUseCase
import com.mscode.domain.cart.usecase.ToggleCartUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideCartMapper(): CartMapper = CartMapper()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cart_database"
        ).build()

    @Provides
    @Singleton
    fun provideCartDao(db: AppDatabase): CartDao = db.cartDao()

    @Provides
    @Singleton
    fun provideCartLocalDataSource(dao: CartDao) = CartLocalDataSource(dao)

    @Provides
    @Singleton
    fun provideCartRepository(
        cartLocalDataSource: CartLocalDataSource,
        mapper: CartMapper
    ): CartRepository = CartRepositoryImpl(cartLocalDataSource, mapper)

    @Provides
    @Singleton
    fun provideGetCartUseCase(repo: CartRepository): GetCartUseCase =
        GetCartUseCase(repo)

    @Provides
    @Singleton
    fun provideTogglePurchaseUseCase(repo: CartRepository): ToggleCartUseCase =
        ToggleCartUseCase(repo)


    @Provides
    @Singleton
    fun provideRemovePurchaseUseCase(repo: CartRepository): RemoveCartProductUseCase =
        RemoveCartProductUseCase(repo)

    @Provides
    @Singleton
    fun provideRemoveAllPurchaseUseCase(repo: CartRepository): RemoveCartUseCase =
        RemoveCartUseCase(repo)

}