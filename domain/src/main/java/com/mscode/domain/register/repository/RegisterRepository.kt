package com.mscode.domain.register.repository

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.register.model.RegisterUser

interface RegisterRepository {

    suspend fun register(registerUser: RegisterUser): WrapperResults<Unit>

}