package com.mscode.domain.common

sealed class WrapperResults<out T>{
    data class Success<T>(val data: T) : WrapperResults<T>()
    data class Error(val exception: Exception) : WrapperResults<Nothing>()
}
