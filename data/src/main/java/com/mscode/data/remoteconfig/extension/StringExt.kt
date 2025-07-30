package com.mscode.data.remoteconfig.extension

internal val String.toUrl get() = this.split("\"")[1]