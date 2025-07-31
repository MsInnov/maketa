package com.mscode.data.login.repository

import com.mscode.data.login.api.LoginApi
import com.mscode.data.login.datasource.LoginLocalDataSource
import com.mscode.data.login.datasource.LoginRemoteDataSource
import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.model.url_products
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.login.repository.LoginRepository

class LoginRepositoryImpl(
    private val localConfigDataSource: LocalConfigDataSource,
    private val loginLocalDataSource: LoginLocalDataSource,
    private val retrofitFactory: RetrofitFactory
): LoginRepository {
    override suspend fun login(login: String, pass: String): WrapperResults<Unit> {
        val baseUrl = localConfigDataSource.urls.firstOrNull { it.key == url_products }
            ?: return WrapperResults.Error(Exception("Product URL missing"))
        val api = retrofitFactory.create(baseUrl.value, LoginApi::class.java)
        val remoteDataSource = LoginRemoteDataSource(api)

        return when (val result = remoteDataSource.login(login, pass)) {
            is WrapperResults.Success -> {
                loginLocalDataSource.saveToken(result.data.token)
                WrapperResults.Success(Unit)
            }
            else -> result as WrapperResults.Error
        }
    }

    override fun getToken(): String? = loginLocalDataSource.token

    override fun removeToken() {
        loginLocalDataSource.saveToken(null)
    }
}