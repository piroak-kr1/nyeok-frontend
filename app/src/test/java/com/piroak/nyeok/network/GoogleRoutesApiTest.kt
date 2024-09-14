package com.piroak.nyeok.network

import com.google.api.gax.rpc.HeaderProvider
import com.google.maps.routing.v2.ComputeRoutesRequest
import com.google.maps.routing.v2.Route
import com.google.maps.routing.v2.RouteTravelMode
import com.google.maps.routing.v2.RoutesClient
import com.google.maps.routing.v2.RoutesSettings
import com.google.maps.routing.v2.RoutingPreference
import com.google.maps.routing.v2.Waypoint
import com.piroak.nyeok.common.Coordinate
import com.piroak.nyeok.ui.demo.getMockRoute
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class GoogleRoutesApiTest {
    @Test
    @Suppress("ObjectLiteralToLambda")
    fun sample() = runTest {
        val routesSettings = RoutesSettings.newBuilder().setHeaderProvider(object : HeaderProvider {
            override fun getHeaders(): MutableMap<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["X-Goog-FieldMask"] = "*"
                return headers
            }
        }).build()
        val routesClient = RoutesClient.create(routesSettings)
        val response = routesClient.computeRoutes(
            ComputeRoutesRequest.newBuilder()
                .setOrigin(Waypoint.newBuilder().setPlaceId("ChIJeRpOeF67j4AR9ydy_PIzPuM").build())
                .setDestination(
                    Waypoint.newBuilder().setPlaceId("ChIJG3kh4hq6j4AR_XuFQnV0_t8").build()
                ).setRoutingPreference(RoutingPreference.TRAFFIC_AWARE)
                .setTravelMode(RouteTravelMode.DRIVE).build()
        )
        println("Response: $response")
    }

    suspend fun posplex_to_apart(): Route {
        val route = GoogleRoutesApi().getTransitRoute(
            origin = Coordinate(36.0192418, 129.3242741).toGoogleLatLng(),
            destination = Coordinate(36.0214277, 129.3370694).toGoogleLatLng()
        )
        return route
    }

    @Test
    fun getTransitRoute_posplex_to_apart() = runTest {
        val route = posplex_to_apart()
        println("Route: $route")
        
//        val json = JsonFormat.printer().print(route)
//        println("Route JSON: $json")
    }

    @Test
    fun checkMockData() = runTest {
        val mockData = getMockRoute()
        val realData = posplex_to_apart()

        assertThat(mockData)
            .usingRecursiveComparison()
            .ignoringFieldsMatchingRegexes(".*arrivalTime.*", ".*departureTime.*")
            .ignoringFieldsMatchingRegexes(".*memoized.*")
            .isEqualTo(realData)
    }
}