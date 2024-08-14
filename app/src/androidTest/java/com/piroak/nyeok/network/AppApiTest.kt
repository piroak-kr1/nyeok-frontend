package com.piroak.nyeok.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.maps.routing.v2.ComputeRoutesResponse
import com.piroak.nyeok.common.Coordinate
import com.piroak.nyeok.ui.demo.getMockRoute
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
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

    @Test
    fun computeRoutesSample() = runTest {
        val mockData = getMockRoute()
        val response: ComputeRoutesResponse = appApiService.computeRoutesSample()

        assertThat(mockData).usingRecursiveComparison()
            .ignoringFieldsMatchingRegexes(".*arrivalTime.*", ".*departureTime.*")
            .ignoringFieldsMatchingRegexes(".*memoized.*").isEqualTo(response.routesList[0])
    }

    @Test
    fun computeRoutes() = runTest {
        val mockData = getMockRoute()
        val request = RouteRequest(
            origin = Coordinate(36.0192418, 129.3242741),
            destination = Coordinate(36.0214277, 129.3370694)
        )
        val response: ComputeRoutesResponse = appApiService.computeRoutes(request)

        assertThat(mockData).usingRecursiveComparison()
            .ignoringFieldsMatchingRegexes(".*arrivalTime.*", ".*departureTime.*")
            .ignoringFieldsMatchingRegexes(".*memoized.*").isEqualTo(response.routesList[0])
    }
}
