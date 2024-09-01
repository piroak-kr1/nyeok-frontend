package com.piroak.nyeok.network

import com.google.maps.routing.v2.ComputeRoutesResponse
import com.piroak.nyeok.common.Coordinate
import com.piroak.nyeok.common.Place
import com.piroak.nyeok.ui.demo.getMockRoute
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test

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

    @Test
    fun placeSample() = runTest {
        val response: Place = appApiService.placeSample()
        val expected: Place = Place(
            title = "가미우동",
            contentid = 2679033,
            coordinate = Coordinate(37.5547407496, 126.9238735556),
            firstimage2 = "http://tong.visitkorea.or.kr/cms/resource/64/2676864_image2_1.jpg",
        )

        assertThat(response).isEqualTo(expected)
    }
}
