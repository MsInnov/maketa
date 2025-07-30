package com.mscode.domain.login.model

sealed class LoginError : Exception() {

    data object ErrorPass: LoginError()
    data object ErrorLogin: LoginError()

}