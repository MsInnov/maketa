package com.mscode.data.register.repository

import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.register.api.RegisterApi
import com.mscode.data.register.datasource.RegisterRemoteDataSource
import com.mscode.data.register.mapper.RegisterMapper
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.model.url_products
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.register.model.RegisterUser
import com.mscode.domain.register.repository.RegisterRepository

class RegisterRepositoryImpl(
    private val localConfigDataSource: LocalConfigDataSource,
    private val retrofitFactory: RetrofitFactory
): RegisterRepository {

    override suspend fun register(registerUser: RegisterUser): WrapperResults<Unit> {
        val baseUrl = localConfigDataSource.urls.firstOrNull { it.key == url_products }
            ?: return WrapperResults.Error(Exception("Product URL missing"))
        val api = retrofitFactory.create(baseUrl.value, RegisterApi::class.java)
        val remoteDataSource = RegisterRemoteDataSource(api, RegisterMapper())

        return when (val result = remoteDataSource.registerUser(registerUser)) {
            is WrapperResults.Success -> WrapperResults.Success(Unit)
            else -> result as WrapperResults.Error
        }
    }

}