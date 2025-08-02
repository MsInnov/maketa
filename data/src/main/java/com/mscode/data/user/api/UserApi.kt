package com.mscode.data.user.api

import com.mscode.data.user.model.UserEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response

interface UserApi {

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): Response<UserEntity>
}