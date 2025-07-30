package com.mscode.data.login.datasource

import com.mscode.data.login.api.LoginApi
import com.mscode.data.login.model.LoginBody
import com.mscode.data.login.model.LoginEntity
import com.mscode.domain.common.WrapperResults

class LoginRemoteDataSource(
    private val api: LoginApi,
) {
    suspend fun login(login: String, pass: String): WrapperResults<LoginEntity> = try {
        WrapperResults.Success(api.login(LoginBody(login, pass)))
    } catch (e: Exception) {
        WrapperResults.Error(e)
    }
}