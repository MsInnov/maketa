package com.mscode.domain.login.usecase

import com.mscode.domain.login.repository.LoginRepository

class GetTokenUseCase(
    private val repository: LoginRepository
) {
    operator fun invoke() = repository.getToken()
}