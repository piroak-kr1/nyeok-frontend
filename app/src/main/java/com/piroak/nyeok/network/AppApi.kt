package com.piroak.nyeok.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object AppApi {
    private const val BASE_URL = "http://10.0.2.2:8000" // localhost
    val retrofitService: AppApiService by lazy {
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()

        retrofit.create(AppApiService::class.java)
    }
}