package com.mscode.data.products.api

import com.mscode.data.products.model.ProductEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductsApi {
    @GET("products")
    suspend fun getProducts(): List<ProductEntity>

    @POST("products")
    suspend fun newProduct(@Body body: ProductEntity): ProductEntity
}