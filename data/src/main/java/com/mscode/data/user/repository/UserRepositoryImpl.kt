package com.mscode.data.user.repository

import com.mscode.data.user.api.UserApi
import com.mscode.data.user.datasource.UserRemoteDataSource
import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.model.url_products
import com.mscode.domain.user.model.User
import com.mscode.domain.user.repository.UserRepository
import com.mscode.domain.common.WrapperResults

class UserRepositoryImpl(
    private val localConfigDataSource: LocalConfigDataSource,
    private val retrofitFactory: RetrofitFactory,
) : UserRepository {

    override suspend fun getUser(): WrapperResults<User> {
        val baseUrl = localConfigDataSource.urls.firstOrNull { it.key == url_products }
            ?: return WrapperResults.Error(Exception("Product URL missing"))
        val api = retrofitFactory.create(baseUrl.value, UserApi::class.java)
        val remoteDataSource = UserRemoteDataSource(api)

        return when (val user = remoteDataSource.getUser()) {
            is WrapperResults.Success -> WrapperResults.Success(
                User(
                    email = user.data?.email ?: "",
                    user = user.data?.username ?: ""
                )
            )

            is WrapperResults.Error -> user
        }
    }
}