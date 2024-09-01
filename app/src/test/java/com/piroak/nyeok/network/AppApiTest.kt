package com.piroak.nyeok.network

import com.google.maps.routing.v2.ComputeRoutesResponse
import com.piroak.nyeok.common.Coordinate
import com.piroak.nyeok.common.Place
import com.piroak.nyeok.common.PlaceAndDistance
import com.piroak.nyeok.common.PlacesResult
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

    @Test
    fun placesClosest() = runTest {
        val sinchonArtreon = Coordinate(37.5565616, 126.9402326)
        val response: PlacesResult = appApiService.placesClosest(user_coordinate = sinchonArtreon)
        val expected: PlacesResult = PlacesResult(
            place_and_distance_list = listOf(
                PlaceAndDistance(
                    place = Place(
                        contentid = 2910892,
                        title = "러너스클럽 이대",
                        coordinate = Coordinate(
                            latitude = 37.5563603535, longitude = 126.9432995176
                        ),
                        firstimage2 = "http://tong.visitkorea.or.kr/cms/resource/12/2889512_image3_1.jpg"
                    ), distance_meter = 271.90817257F
                ), PlaceAndDistance(
                    place = Place(
                        contentid = 2929386,
                        title = "EW 스파오 신촌",
                        coordinate = Coordinate(
                            latitude = 37.5550492645, longitude = 126.9360168847
                        ),
                        firstimage2 = "http://tong.visitkorea.or.kr/cms/resource/53/2878353_image3_1.jpg"
                    ), distance_meter = 408.57002041F
                ), PlaceAndDistance(
                    place = Place(
                        contentid = 3305576,
                        title = "안경진정성이마트신촌점",
                        coordinate = Coordinate(
                            latitude = 37.5550492645, longitude = 126.9360168847
                        ),
                        firstimage2 = "http://tong.visitkorea.or.kr/cms/resource/98/3312998_image3_1.jpg"
                    ), distance_meter = 408.57002041F
                )
            )
        )

        assertThat(response).isEqualTo(expected)
    }
}
