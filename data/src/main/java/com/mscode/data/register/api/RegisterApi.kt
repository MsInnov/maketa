package com.mscode.data.register.api

import com.mscode.data.register.model.RegisterBody
import com.mscode.data.register.model.RegisterEntity
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {
    @POST("users")
    suspend fun addUser(@Body body: RegisterBody): RegisterEntity
}