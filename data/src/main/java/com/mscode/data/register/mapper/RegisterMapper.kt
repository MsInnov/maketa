package com.mscode.data.register.mapper

import com.mscode.data.register.model.RegisterBody
import com.mscode.domain.register.model.RegisterUser

class RegisterMapper {
    fun toRegisterBody(id: Int, registerUser: RegisterUser) = RegisterBody(
        id = id,
        username = registerUser.login,
        password = registerUser.pass,
        email = registerUser.email
    )
}