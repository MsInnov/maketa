package com.mscode.domain.login.usecase

import com.mscode.domain.login.repository.LoginRepository

class RemoveTokenUseCase (
    private val repository: LoginRepository
) {
    operator fun invoke() = repository.removeToken()
}