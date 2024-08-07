package com.piroak.nyeok.network

import androidx.test.core.app.ApplicationProvider
import com.piroak.nyeok.GlobalApplication
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class KakaoLocalApiTest {
    private val application: GlobalApplication =
        ApplicationProvider.getApplicationContext() as GlobalApplication
    private val kakaoLocalApiService = KakaoLocalApi.retrofitService

    @Test
    fun getRetrofitService() = runTest {
        val response: SearchByKeywordResponse = kakaoLocalApiService.searchByKeyword(
            restApiKey = "KakaoAK ${application.env.kakaoRestApiKey}", keywordQuery = "교동면옥"
        )

        assertEquals(80, response.meta.total_count)
        assertEquals(45, response.meta.pageable_count)
        assertEquals(15, response.documents.size)
    }
}