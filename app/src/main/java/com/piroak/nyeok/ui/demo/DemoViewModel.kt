package com.piroak.nyeok.ui.demo

import android.Manifest.permission
import androidx.lifecycle.ViewModel
import com.kakao.vectormap.LatLng
import com.piroak.nyeok.data.ILocationOrientationProvider
import com.piroak.nyeok.permission.IPermissionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
}