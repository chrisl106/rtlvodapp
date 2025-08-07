package com.example.rtlvodapp.model

data class LoginResponse(
    val token: String? = null,  // Fixed: Token if success, null if fail
    val message: String? = null  // Fixed: Message if fail, null if success
)