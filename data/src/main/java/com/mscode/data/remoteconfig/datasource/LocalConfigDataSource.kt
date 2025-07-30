package com.mscode.data.remoteconfig.datasource

import com.mscode.data.remoteconfig.model.Url

class LocalConfigDataSource {

    private val _urls = emptyList<Url>().toMutableList()
    val urls = _urls

    fun saveUrl(url: Url) {
        _urls.add(url)
    }

}