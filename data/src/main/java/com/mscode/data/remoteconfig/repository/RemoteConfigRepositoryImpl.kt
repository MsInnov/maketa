package com.mscode.data.remoteconfig.repository

import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.datasource.RemoteConfigDataSource
import com.mscode.data.remoteconfig.model.Url
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.remoteconfig.repository.RemoteConfigRepository
import java.lang.Exception

class RemoteConfigRepositoryImpl(
    private val remoteConfigDataSource: RemoteConfigDataSource,
    private val localConfigDataSource: LocalConfigDataSource
) : RemoteConfigRepository {

    override suspend fun updateRemoteConfig(): WrapperResults<Unit> =
        remoteConfigDataSource.getRemoteConfig().let { urls ->
            urls?.let {
                urls.map { url ->
                    localConfigDataSource.saveUrl(
                        Url(url.first, url.second)
                    )
                }
                WrapperResults.Success(Unit)
            } ?: WrapperResults.Error(Exception())
        }
}