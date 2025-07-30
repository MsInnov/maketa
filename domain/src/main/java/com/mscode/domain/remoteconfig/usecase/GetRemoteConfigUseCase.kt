package com.mscode.domain.remoteconfig.usecase

import com.mscode.domain.remoteconfig.repository.RemoteConfigRepository

class GetRemoteConfigUseCase(
    private val remoteConfigRepository: RemoteConfigRepository
) {

    suspend operator fun invoke() = remoteConfigRepository.updateRemoteConfig()

}