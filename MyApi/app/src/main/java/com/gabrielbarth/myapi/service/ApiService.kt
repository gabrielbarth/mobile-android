package com.gabrielbarth.myapi.service

import com.gabrielbarth.myapi.model.Item
import com.gabrielbarth.myapi.model.ItemValue
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("items")
    suspend fun getItems(): List<Item>

    @GET("items/{id}")
    suspend fun getItem(@Path("id") id: String): Item

    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") id: String)

    @POST("items")
    suspend fun addItem(@Body item: ItemValue): Item
}