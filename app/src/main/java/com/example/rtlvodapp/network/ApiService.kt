package com.example.rtlvodapp.network

import com.example.rtlvodapp.model.LoginRequest
import com.example.rtlvodapp.model.LoginResponse
import com.example.rtlvodapp.model.VideoItem
import com.example.rtlvodapp.model.UpdateVersion  // New: Import the version note
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Header

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("videos")
    suspend fun getVideos(@Header("Authorization") token: String): Response<List<VideoItem>>

    @GET("app/version.json")  // New: Call to get the version note from your site
    suspend fun getUpdateVersion(): UpdateVersion
}