package com.example.lurk.api

data class AuthResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val timeTillExpiration: Long? = null,
    val scope: String? = null,
    val userless: Boolean = false
)