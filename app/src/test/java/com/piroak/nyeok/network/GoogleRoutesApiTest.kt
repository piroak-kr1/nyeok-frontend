package com.piroak.nyeok.network

import com.google.api.gax.rpc.HeaderProvider
import com.google.maps.routing.v2.ComputeRoutesRequest
import com.google.maps.routing.v2.RouteTravelMode
import com.google.maps.routing.v2.RoutesClient
import com.google.maps.routing.v2.RoutesSettings
import com.google.maps.routing.v2.RoutingPreference
import com.google.maps.routing.v2.Waypoint
import com.piroak.nyeok.common.LatLng
import org.junit.Test

class GoogleRoutesApiTest {
    @Test
    @Suppress("ObjectLiteralToLambda")
    fun sample() {
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

    @Test
    fun getTransitRoute_posplex_to_apart() {
        val route = GoogleRoutesApi().getTransitRoute(
            origin = LatLng(36.0192418, 129.3242741).toGoogleLatLng(),
            destination = LatLng(36.0214277, 129.3370694).toGoogleLatLng()
        )
        println("Route: $route")
    }
}