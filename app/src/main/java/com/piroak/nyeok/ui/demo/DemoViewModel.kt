package com.piroak.nyeok.ui.demo

import android.Manifest.permission
import androidx.lifecycle.ViewModel
import com.piroak.nyeok.data.ILocationOrientationProvider
import com.piroak.nyeok.data.LocationOrientationProvider
import com.piroak.nyeok.permission.IPermissionManager
import com.piroak.nyeok.permission.PermissionManager
import kotlinx.coroutines.flow.StateFlow

class DemoViewModel(
    locationOrientationProvider: ILocationOrientationProvider,
    private val permissionManager: IPermissionManager
) : ViewModel() {
    val locationPermissionFlow: StateFlow<Boolean> = permissionManager.locationPermissionFlow
    val orientationFlow = locationOrientationProvider.orientationFlow
    val locationFlow = locationOrientationProvider.locationFlow

    fun requestLocationPermission() {
        permissionManager.requestPermissions(
            arrayOf(
                permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}