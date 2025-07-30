package com.mscode.data.register.datasource

import com.mscode.data.register.api.RegisterApi
import com.mscode.data.register.mapper.RegisterMapper
import com.mscode.data.register.model.RegisterEntity
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.register.model.RegisterUser

class RegisterRemoteDataSource(
    private val api: RegisterApi,
    private val registerMapper: RegisterMapper
) {
    suspend fun registerUser(registerUser: RegisterUser): WrapperResults<RegisterEntity> = try {
        WrapperResults.Success(api.addUser(registerMapper.toRegisterBody(id = 0, registerUser)))
    } catch (e: Exception) {
        WrapperResults.Error(e)
    }
}