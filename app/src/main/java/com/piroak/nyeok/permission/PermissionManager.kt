package com.piroak.nyeok.permission

import android.Manifest
import android.Manifest.permission
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.piroak.nyeok.GlobalApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PermissionManager(
    private val globalApplication: GlobalApplication
) {
    private val permissions = arrayOf(
        permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION
    )
    private val _grantedStates: Map<String, MutableStateFlow<Boolean>> = permissions.associateWith {
        MutableStateFlow(value = checkPermission(it))
    }

    val locationPermissionFlow: StateFlow<Boolean> =
        getPermissionFlow(permission.ACCESS_FINE_LOCATION)
    
    fun getPermissionFlow(permission: String): StateFlow<Boolean> = _grantedStates[permission]!!

    fun requestPermissions(requests: Array<String>) {
        val activity: ComponentActivity = globalApplication.currentActivity!!
        val activityResultRegistry = activity.activityResultRegistry

        if (requests.any { !permissions.contains(it) }) {
            Log.e("GUN", "Permissions not expected: ${requests.joinToString()}")
            return
        }
        
        // Create a launcher for requesting multiple permissions
        // Callback = updateAllPermissionStates
        val activityResultLauncher = activityResultRegistry.register(
            "requestPermission", ActivityResultContracts.RequestMultiplePermissions()
        ) {
            updateAllPermissionStates()
        }
        
        // Launch the permission request
        activityResultLauncher.launch(
            requests
        )
    }
    
    private fun checkPermission(permission: String): Boolean {
        if (!permissions.contains(permission)) {
            Log.e("GUN", "Permission not expected: $permission")
            return false
        }

        return when(ContextCompat.checkSelfPermission(globalApplication, permission)) {
            PackageManager.PERMISSION_GRANTED -> true
            else -> false
        }
    }

    private fun updatePermissionState(permission: String) {
        if (!permissions.contains(permission)) {
            Log.e("GUN", "Permission not expected: $permission")
            return
        }

        _grantedStates[permission]!!.value = checkPermission(permission)
    }

    private fun updateAllPermissionStates() {
        permissions.forEach {
            updatePermissionState(it)
        }
    }
}