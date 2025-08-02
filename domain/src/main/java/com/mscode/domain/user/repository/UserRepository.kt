package com.mscode.domain.user.repository

import com.mscode.domain.user.model.User
import com.mscode.domain.common.WrapperResults

interface UserRepository {

    suspend fun getUser(): WrapperResults<User>

}