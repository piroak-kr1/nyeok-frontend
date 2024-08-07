package com.piroak.nyeok.ui.demo

import android.Manifest.permission
import android.location.Location
import androidx.lifecycle.ViewModel
import com.kakao.vectormap.LatLng
import com.piroak.nyeok.data.ILocationOrientationProvider
import com.piroak.nyeok.permission.IPermissionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class DemoViewModel(
    locationOrientationProvider: ILocationOrientationProvider,
    private val permissionManager: IPermissionManager
) : ViewModel() {
    val locationPermissionFlow: StateFlow<Boolean> = permissionManager.locationPermissionFlow
    val locationFlow: StateFlow<LatLng?> = makeStateFlow(
        initialValue = null,
        flow = locationOrientationProvider.locationFlow.map {
            it?.let { LatLng.from(it.latitude, it.longitude) }
        },
    )
    val orientationFlow: StateFlow<Float?> = makeStateFlow(
        initialValue = null,
        flow = locationOrientationProvider.deviceOrientationFlow.map {
            it?.headingDegrees
        },
    )
    private val _destinationFlow: MutableStateFlow<LatLng?> = MutableStateFlow(null)
    val destinationFlow: StateFlow<LatLng?> = _destinationFlow

    /**
     * StateFlow of (distance, bearing)
     */
    val straightPathFlow: StateFlow<Pair<Float, Float>?> = makeStateFlow(
        initialValue = null,
        flow = combine(
            locationFlow.filterNotNull(), destinationFlow.filterNotNull()
        ) { location, destination ->
            calculateStraightPath(location, destination)
        },
    )

    fun requestLocationPermission() {
        permissionManager.requestPermissions(
            arrayOf(
                permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    fun setDestination(destination: LatLng) {
        _destinationFlow.value = destination
    }

    /**
     * Calculate the distance between the current location and the destination.
     * bearing: degrees east of true north from locationA to locationB
     * @return (distance, bearing)
     */
    private fun calculateStraightPath(start: LatLng, end: LatLng): Pair<Float, Float> {
        val results = FloatArray(2)
        Location.distanceBetween(
            start.latitude, start.longitude, end.latitude, end.longitude, results
        )
        return Pair(results[0], results[1])
    }
}