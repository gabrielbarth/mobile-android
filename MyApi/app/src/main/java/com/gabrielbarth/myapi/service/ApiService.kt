package com.gabrielbarth.myapi.service

import com.gabrielbarth.myapi.model.Item
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("items")
    suspend fun getItems(): List<Item>

    @GET("items/{id}")
    suspend fun getItem(@Path("id") id: String): Item
}