package com.piroak.nyeok.common

data class PlacesResult(
    val place_and_distance_list: List<PlaceAndDistance>
)

data class PlaceAndDistance(
    val distance_meter: Float, val place: Place
)