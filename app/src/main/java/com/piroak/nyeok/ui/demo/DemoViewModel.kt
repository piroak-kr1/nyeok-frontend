package com.piroak.nyeok.ui.demo

import android.Manifest.permission
import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.maps.routing.v2.RouteLegStep
import com.kakao.vectormap.LatLng
import com.piroak.nyeok.data.ILocationOrientationProvider
import com.piroak.nyeok.network.toCommonLatLng
import com.piroak.nyeok.permission.IPermissionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

fun com.piroak.nyeok.common.Coordinate.toKakaoLatLng(): LatLng =
    LatLng.from(this.latitude, this.longitude)

class DemoViewModel(
    locationOrientationProvider: ILocationOrientationProvider,
    private val permissionManager: IPermissionManager
) : ViewModel() {
    // User input
    private val _destinationFlow: MutableStateFlow<LatLng?> = MutableStateFlow(null)
    val destinationFlow: StateFlow<LatLng?> = _destinationFlow

    // User information
    val locationPermissionFlow: StateFlow<Boolean> = permissionManager.locationPermissionFlow
    val locationFlow: StateFlow<LatLng?> = makeStateFlow(
        initialValue = null,
        flow = locationOrientationProvider.locationFlow.map {
            it?.let { LatLng.from(it.latitude, it.longitude) }
        },
    )
    val userHeadingDegreesFlow: StateFlow<Float?> = makeStateFlow(
        initialValue = null,
        flow = locationOrientationProvider.deviceOrientationFlow.map {
            it?.headingDegrees
        },
    )

    // Result of Routes API
    private val _stepsListFlow: MutableStateFlow<List<RouteLegStep>?> =
        MutableStateFlow(getMockRoute().legsList[0].stepsList)
    private val _stepIndexFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    val currentStepFlow: StateFlow<RouteLegStep?> = makeStateFlow(
        initialValue = null,
        flow = _stepIndexFlow.map { index ->
            _stepsListFlow.value?.getOrNull(index)
        },
    )

    /**
     * StateFlow of (distance in meter, bearing in degrees)
     */
    val straightPathFlow: StateFlow<Pair<Float, Float>?> = makeStateFlow(
        initialValue = null,
        flow = combine(
            locationFlow.filterNotNull(), currentStepFlow.filterNotNull()
        ) { location, step ->
            calculateStraightPath(
                location,
                step.endLocation.latLng.toCommonLatLng().toKakaoLatLng()
            )
        },
    )

    fun nextStep() {
        val stepsList = _stepsListFlow.value
        if (stepsList.isNullOrEmpty()) return
        _stepIndexFlow.value =
            (_stepIndexFlow.value + 1).coerceAtMost(maximumValue = stepsList.lastIndex)
    }

    fun prevStep() {
        _stepIndexFlow.value = (_stepIndexFlow.value - 1).coerceAtLeast(minimumValue = 0)
    }

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