package com.mscode.domain.login.repository

import com.mscode.domain.common.WrapperResults

interface LoginRepository {

    suspend fun login(login: String, pass: String): WrapperResults<Unit>
    fun getToken(): String?
    fun removeToken()

}