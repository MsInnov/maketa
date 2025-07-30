package com.mscode.domain.remoteconfig.repository

import com.mscode.domain.common.WrapperResults

interface RemoteConfigRepository {

    suspend fun updateRemoteConfig(): WrapperResults<Unit>

}