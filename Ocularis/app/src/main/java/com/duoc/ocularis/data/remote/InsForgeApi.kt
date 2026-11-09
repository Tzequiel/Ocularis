package com.duoc.ocularis.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

data class LoginRequest(
    @SerializedName("method") val method: String = "password",
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("email_verified", alternate = ["emailVerified"]) val emailVerified: Boolean = false
)

data class LoginResponse(
    @SerializedName("user") val user: UserDto,
    @SerializedName("access_token", alternate = ["accessToken"]) val accessToken: String,
    @SerializedName("refresh_token", alternate = ["refreshToken"]) val refreshToken: String? = null
)

interface InsForgeApiService {
    @Headers("Content-Type: application/json")
    @POST("api/auth/sessions")
    suspend fun login(
        @Header("Authorization") authorization: String,
        @Header("apikey") apikey: String,
        @Body request: LoginRequest
    ): Response<LoginResponse>
}
