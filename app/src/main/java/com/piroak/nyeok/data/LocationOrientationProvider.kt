package com.piroak.nyeok.data

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.DeviceOrientation
import com.google.android.gms.location.DeviceOrientationListener
import com.google.android.gms.location.DeviceOrientationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.piroak.nyeok.GlobalApplication
import com.piroak.nyeok.permission.PermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.Executors

class LocationOrientationProvider(
    globalApplication: GlobalApplication,
    externalScope: CoroutineScope,
    permissionManager: PermissionManager
) {
    private val locationPermissionFlow: StateFlow<Boolean> = permissionManager.locationPermissionFlow

    private val fusedOrientationProviderClient = LocationServices.getFusedOrientationProviderClient(globalApplication)
    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(globalApplication)
    
    // awaitClose block will be executed when collector stops
    // rerun callbackFlow block when new collector starts
    private val _orientationFlow: Flow<DeviceOrientation> = callbackFlow {
        val deviceOrientationRequest =
            DeviceOrientationRequest.Builder(/* samplingPeriodMicros = */ 500_000).build()
        
        val deviceOrientationListener = DeviceOrientationListener {
            deviceOrientationResult -> this@callbackFlow.trySend(deviceOrientationResult)
        }

        fusedOrientationProviderClient.requestOrientationUpdates(
            deviceOrientationRequest, Executors.newSingleThreadExecutor(), deviceOrientationListener
        )
        
        // Run flow until closed, then cleanup API
        this.awaitClose { fusedOrientationProviderClient.removeOrientationUpdates(deviceOrientationListener) }
    }
    // Stop collecting from _orientationFlow when there is no subscribers
    val orientationFlow: StateFlow<DeviceOrientation?> = _orientationFlow.stateIn(
        externalScope, started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000), initialValue = null
    )
    
    // Location Flow
    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _locationFlow: Flow<Location?> = locationPermissionFlow.flatMapLatest { hasPermission ->
        if (hasPermission) {
            callbackFlow<Location> {
                val locationRequest = LocationRequest.Builder(/* intervalMillis = */ 500).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        if (locationResult.lastLocation != null) {
                            trySend(locationResult.lastLocation!!)
                        }
                    }
                }

                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest, locationCallback, Looper.getMainLooper()
                )

                awaitClose { fusedLocationProviderClient.removeLocationUpdates(locationCallback) }
            }
        } else {
            flowOf(null)
        }
    }
    val locationFlow: StateFlow<Location?> = _locationFlow.stateIn(
        externalScope, started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000), initialValue = null
    )
    
}