package com.piroak.nyeok.network

import retrofit2.http.GET
import retrofit2.http.Query

interface AppApiService {
    @GET("/")
    suspend fun hello(): String

    @GET("/echo")
    suspend fun echo(@Query("message") message: String): String
}
