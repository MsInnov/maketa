package com.mscode.data.remoteconfig.model

const val url_products = "url_get_products"

val urls = listOf(url_products)

data class Url (
    val key: String,
    val value: String
)