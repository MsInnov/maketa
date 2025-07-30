package com.mscode.data.login.datasource

class LoginLocalDataSource {

    private var _token: String? = null

    val token: String?
        get() = _token

    fun saveToken(token: String?) {
        _token = token
    }

}