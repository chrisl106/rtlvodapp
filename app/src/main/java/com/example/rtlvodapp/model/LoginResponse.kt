package com.example.rtlvodapp.model

data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val message: String?
)
