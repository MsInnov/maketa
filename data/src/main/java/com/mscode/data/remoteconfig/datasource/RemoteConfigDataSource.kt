package com.mscode.data.remoteconfig.datasource

import com.mscode.data.remoteconfig.extension.toUrl
import com.mscode.data.remoteconfig.model.urls
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class RemoteConfigDataSource(
    private val remoteConfig: FirebaseRemoteConfig
) {

    suspend fun getRemoteConfig(): List<Pair<String, String>>? =
        suspendCancellableCoroutine { cont ->
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val urlsResponse = urls.map { url ->
                        url to remoteConfig.getString(url).toUrl
                    }
                    cont.resume(urlsResponse)
                } else {
                    cont.resume(null)
                }
            }
        }

}