package com.piroak.nyeok.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KakaoLocalApi {
    private const val BASE_URL = "https://dapi.kakao.com/v2/local/"
    val retrofitService: KakaoLocalApiService by lazy {
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()

        retrofit.create(KakaoLocalApiService::class.java)
    }
}