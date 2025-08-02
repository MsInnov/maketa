package com.mscode.data.user.datasource

import com.mscode.data.user.api.UserApi
import com.mscode.domain.common.WrapperResults
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val api: UserApi
) {

    suspend fun getUser() = try {
        val response = api.getUser(1)
        WrapperResults.Success(response.body())
    } catch (e: Exception) {
        WrapperResults.Error(e)
    }

}