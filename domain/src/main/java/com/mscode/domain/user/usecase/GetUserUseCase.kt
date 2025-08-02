package com.mscode.domain.user.usecase

import com.mscode.domain.user.repository.UserRepository

class GetUserUseCase(
    private val repo: UserRepository
) {

    suspend operator fun invoke() = repo.getUser()

}