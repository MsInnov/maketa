package com.mscode.data.login.api

import com.mscode.data.login.model.LoginBody
import com.mscode.data.login.model.LoginEntity
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/login")
    suspend fun login(@Body body: LoginBody): LoginEntity
}