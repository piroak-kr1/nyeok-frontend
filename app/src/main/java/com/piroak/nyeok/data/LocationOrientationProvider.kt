package com.piroak.nyeok.data

import com.google.android.gms.location.DeviceOrientation
import com.google.android.gms.location.DeviceOrientationListener
import com.google.android.gms.location.DeviceOrientationRequest
import com.google.android.gms.location.LocationServices
import com.piroak.nyeok.GlobalApplication
import com.piroak.nyeok.permission.PermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.Executors

class LocationOrientationProvider(
    globalApplication: GlobalApplication,
    externalScope: CoroutineScope,
    private val permissionManager: PermissionManager
) {
    private val fusedOrientationProviderClient = LocationServices.getFusedOrientationProviderClient(globalApplication)
    
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
    
}