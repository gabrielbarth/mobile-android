package com.gabrielbarth.myapi.service

import com.gabrielbarth.myapi.model.Item
import retrofit2.http.GET

interface ApiService {
    @GET("items")
    suspend fun getItems(): List<Item>
}