package com.mscode.domain.login.usecase

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.login.model.LoginError
import com.mscode.domain.login.repository.LoginRepository
import java.lang.Exception

class LoginUseCase(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(login: String, pass: String) = when {
        pass.isBlank() && login.isBlank() -> WrapperResults.Error(Exception())
        pass.isBlank() -> WrapperResults.Error(LoginError.ErrorPass)
        login.isBlank() -> WrapperResults.Error(LoginError.ErrorLogin)
        else -> repository.login(login, pass)
    }
}