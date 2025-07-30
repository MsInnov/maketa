package com.mscode.domain.register.usecase

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.register.model.RegisterError
import com.mscode.domain.register.model.RegisterUser
import com.mscode.domain.register.repository.RegisterRepository
import java.lang.Exception

class RegisterUseCase(
    private val registerRepository: RegisterRepository
) {

    suspend operator fun invoke(
        email: String,
        pass: String,
        login: String
    ): WrapperResults<Unit> = when {
        email.isBlank() && pass.isBlank() && login.isBlank() -> WrapperResults.Error(Exception())
        email.isBlank() -> WrapperResults.Error(RegisterError.ErrorEmail)
        pass.isBlank() -> WrapperResults.Error(RegisterError.ErrorPass)
        login.isBlank() -> WrapperResults.Error(RegisterError.ErrorLogin)
        else -> registerRepository.register(RegisterUser(login, pass, email))
    }

}