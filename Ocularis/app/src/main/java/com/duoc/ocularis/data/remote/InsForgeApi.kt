package com.duoc.ocularis.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class LoginRequest(
    val method: String = "password",
    val email: String,
    val password: String
)

data class UserDto(
    val id: String,
    val email: String,
    val emailVerified: Boolean
)

data class LoginResponse(
    val user: UserDto,
    val accessToken: String,
    val refreshToken: String? = null
)

interface InsForgeApiService {
    @POST("api/auth/sessions")
    suspend fun login(
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body request: LoginRequest
    ): Response<LoginResponse>
}
