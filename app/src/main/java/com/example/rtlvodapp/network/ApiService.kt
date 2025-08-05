package com.example.rtlvodapp.network

import com.example.rtlvodapp.model.LoginRequest
import com.example.rtlvodapp.model.LoginResponse
import com.example.rtlvodapp.model.VideoItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/videos")
    suspend fun getVideos(): Response<List<VideoItem>>
}
