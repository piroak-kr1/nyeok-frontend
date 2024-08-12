package com.piroak.nyeok.network

import com.google.maps.routing.v2.ComputeRoutesRequest
import com.google.maps.routing.v2.Location
import com.google.maps.routing.v2.Route
import com.google.maps.routing.v2.RouteTravelMode
import com.google.maps.routing.v2.RoutesClient
import com.google.maps.routing.v2.RoutesSettings
import com.google.maps.routing.v2.Units
import com.google.maps.routing.v2.Waypoint
import com.google.type.LatLng

fun com.piroak.nyeok.common.LatLng.toGoogleLatLng(): LatLng = LatLng.newBuilder().run {
    this.latitude = this@toGoogleLatLng.latitude
    this.longitude = this@toGoogleLatLng.longitude
    build()
}

class GoogleRoutesApi {
    fun getTransitRoute(
        origin: LatLng,
        destination: LatLng,
    ): Route {
        val routesSettings = RoutesSettings.newBuilder().setHeaderProvider {
            val headers: MutableMap<String, String> = HashMap()
            headers["X-Goog-FieldMask"] = "*"
            headers
        }.build()
        val routesClient = RoutesClient.create(routesSettings)
        val response = routesClient.computeRoutes(ComputeRoutesRequest.newBuilder().run {
            this.origin = Waypoint.newBuilder().run {
                location = Location.newBuilder().run {
                    latLng = origin
                    build()
                }
                build()
            }
            this.destination = Waypoint.newBuilder().run {
                location = Location.newBuilder().run {
                    latLng = destination
                    build()
                }
                build()
            }
            travelMode = RouteTravelMode.TRANSIT
            units = Units.METRIC
            build()
        })
        
        // TODO: IndexOutOfBoundsException
        val route = response.routesList[0]
        return if (route != null) route else throw KotlinNullPointerException()
    }
}