package com.mscode.domain.register.model

sealed class RegisterError : Exception() {

    data object ErrorEmail: RegisterError()
    data object ErrorPass: RegisterError()
    data object ErrorLogin: RegisterError()

}