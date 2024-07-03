package com.piroak.nyeok.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppApiTest {
    private val appApiService = AppApi.retrofitService

    @Test
    fun hello() = runTest {
        val response = appApiService.hello()
        assertEquals("Hello, world!", response)
    }

    @Test
    fun echo() = runTest {
        val response = appApiService.echo("aabb")
        assertEquals("aabb", response)
    }

    @Test
    fun echo_korean() = runTest {
        val response = appApiService.echo("한글")
        assertEquals("한글", response)
    }
}
