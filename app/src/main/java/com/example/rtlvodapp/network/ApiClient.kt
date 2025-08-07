package com.example.rtlvodapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://rtlvod.com/api/") // Fixed: Removed :3000, using standard secure port 443
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}