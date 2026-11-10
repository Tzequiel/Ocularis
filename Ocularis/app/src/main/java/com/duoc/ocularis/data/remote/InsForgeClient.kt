package com.duoc.ocularis.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object InsForgeClient {
    private const val BASE_URL = "https://43z7p4rz.us-east.insforge.app/"
    const val API_KEY = "ik_6435c4d2b409f587c6ffe2b2c5d5adce"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val api: InsForgeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InsForgeApiService::class.java)
    }
}
