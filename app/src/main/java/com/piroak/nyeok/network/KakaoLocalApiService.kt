package com.piroak.nyeok.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoLocalApiService {
    @GET("search/keyword.json")
    suspend fun searchByKeyword(
        @Header("Authorization") restApiKey: String,
        @Query("query") keywordQuery: String,
    ): SearchByKeywordResponse
}